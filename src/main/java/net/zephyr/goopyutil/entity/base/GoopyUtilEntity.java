package net.zephyr.goopyutil.entity.base;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.zephyr.goopyutil.blocks.computer.ComputerData;
import net.zephyr.goopyutil.init.ItemInit;
import net.zephyr.goopyutil.item.EntitySpawnItem;
import net.zephyr.goopyutil.networking.PayloadDef;
import net.zephyr.goopyutil.networking.payloads.SetNbtS2CPayload;
import net.zephyr.goopyutil.util.Computer.ComputerAI;
import net.zephyr.goopyutil.util.ItemNbtUtil;
import net.zephyr.goopyutil.util.ScreenUtils;
import net.zephyr.goopyutil.util.jsonReaders.entity_skins.EntityDataManager;
import net.zephyr.goopyutil.util.mixinAccessing.IEntityDataSaver;
import net.zephyr.goopyutil.util.mixinAccessing.IGetClientManagers;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.*;

public abstract class GoopyUtilEntity extends PathAwareEntity implements GeoEntity {
    private AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    boolean running = false;
    int runningCooldown = 0;
    boolean crawling = false;
    int crawlingCooldown = 0;
    boolean deactivated = false;
    public boolean menuTick = false;
    private String behavior;

    public GoopyUtilEntity(EntityType<? extends PathAwareEntity> type, World world) {
        super(type, world);
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {

        ((IEntityDataSaver)this).getPersistentData().putLong("spawnPos", getBlockPos().asLong());
        ((IEntityDataSaver)this).getPersistentData().putFloat("spawnRot", getHeadYaw());
        return super.initialize(world, difficulty, spawnReason, entityData);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Main", 3, this::movementAnimController)
                .triggerableAnim(demoAnim(), RawAnimation.begin().thenLoop(demoAnim()))
                .triggerableAnim(activatingAnim(), RawAnimation.begin().thenPlay(attackAnim())));
        controllers.add(new AnimationController<>(this, "Attack", 1, this::attackAnimController));
    }

    private <E extends GoopyUtilEntity> PlayState attackAnimController(AnimationState<E> event) {
        if(this.handSwinging && event.getController().getAnimationState().equals(AnimationController.State.STOPPED)) {
            event.getController().forceAnimationReset();
            this.handSwinging = false;
            return event.setAndContinue(RawAnimation.begin().thenPlay(attackAnim()));
        }
        return PlayState.CONTINUE;
    }

    public <E extends GoopyUtilEntity> PlayState movementAnimController(final AnimationState<E> event) {
        if(event.getAnimatable().isDead()){
            event.getController().setAnimationSpeed(1.1);
            event.getController().transitionLength(0);
            return event.setAndContinue(RawAnimation.begin().thenPlayAndHold(dyingAnim()));
        }
        else {
            event.getController().transitionLength(3);
            String idle = defaultIdleAnim();
            String walking = defaultWalkingAnim();
            String running = defaultRunningAnim();
            String deactivating = deactivatingAnim();
            String deactivated = deactivatedAnim();
            String activating = activatingAnim();
            RawAnimation wakeUp = RawAnimation.begin().thenPlay(activating).thenLoop(idle);
            RawAnimation fallAsleep = RawAnimation.begin().thenPlay(deactivating).thenLoop(deactivated);

            if (boolData(behavior, "custom_moving_animation", this)) {
                idle = stringData(behavior, "idle_animation", this);
                walking = stringData(behavior, "walking_animation", this);
                running = stringData(behavior, "running_animation", this);
            } else {
                if (boolData(behavior, "aggressive", this)) {
                    idle = aggressiveIdleAnim();
                    walking = aggressiveWalkingAnim();
                    running = aggressiveRunningAnim();
                }
                if (boolData(behavior, "performing", this)) {
                    idle = performingAnim();
                }
            }


            if (this.crawling) {
                if (event.isMoving())
                    return event.setAndContinue(RawAnimation.begin().thenLoop(crawlingWalkingAnim()));
                else
                    return event.setAndContinue(RawAnimation.begin().thenLoop(crawlingIdleAnim()));
            } else {
                if (this.deactivated) {
                    if (event.isMoving())
                        return event.setAndContinue(RawAnimation.begin().thenLoop(defaultWalkingAnim()));
                    else {
                        event.getController().transitionLength(0);
                        return event.setAndContinue(fallAsleep);
                    }
                } else if (event.getController().getCurrentRawAnimation() != null && (event.getController().getCurrentRawAnimation().equals(fallAsleep) || (event.getController().getCurrentRawAnimation().equals(wakeUp) && event.getController().getCurrentAnimation().animation().loopType() != Animation.LoopType.LOOP))) {
                    event.getController().transitionLength(0);
                    return event.setAndContinue(wakeUp);
                } else if (Objects.equals(behavior, "statue")) {
                    if (event.isMoving())
                        return event.setAndContinue(RawAnimation.begin().thenLoop(defaultWalkingAnim()));
                    else {
                        String anim = stringData(behavior, "animation", this);
                        return event.setAndContinue(RawAnimation.begin().thenLoop(anim));
                    }
                } else if (Objects.equals(behavior, "stage")) {
                    if (event.isMoving()) {
                        return event.setAndContinue(RawAnimation.begin().thenLoop(defaultWalkingAnim()));
                    } else {
                        return event.setAndContinue(RawAnimation.begin().thenLoop(idle));
                    }

                } else if (Objects.equals(behavior, "moving")) {
                    if (event.isMoving()) {
                        if (this.running)
                            return event.setAndContinue(RawAnimation.begin().thenLoop(running));
                        else
                            return event.setAndContinue(RawAnimation.begin().thenLoop(walking));
                    } else
                        return event.setAndContinue(RawAnimation.begin().thenLoop(idle));
                } else {
                    return event.setAndContinue(RawAnimation.begin().thenLoop(idle));
                }
            }
        }
        //return PlayState.STOP;
    }

    @Override
    public void tick() {
        super.tick();
        syncFromServer(getWorld());
        this.behavior = getAIMode(this, getWorld());
        this.deactivated = behavior.isEmpty() || boolData(behavior, "deactivated", this) || ((IEntityDataSaver)this).getPersistentData().getBoolean("Deactivated");

        runTick();
        if(canCrawl()){
            crawlTick();
        }
        if(behavior.equals("statue") || behavior.equals("stage")){
            goalPosTick();
        }
    }

    void syncFromServer(World world){
        if(!world.isClient()){
            NbtCompound nbt = new NbtCompound();
            nbt.put("data", ((IEntityDataSaver)this).getPersistentData());
            nbt.putInt("entityID", getId());
            for (ServerPlayerEntity p : PlayerLookup.all(getServer())) {
                ServerPlayNetworking.send(p, new SetNbtS2CPayload(nbt, PayloadDef.ENTITY_DATA));
            }
        }
    }

    void goalPosTick() {
        BlockPos goalPos = !boolData(behavior, "spawn_pos", this) ? blockPosData(behavior, "position", this) : BlockPos.fromLong(((IEntityDataSaver) this).getPersistentData().getLong("spawnPos"));
        double offsetX = Math.clamp(intData(behavior, "position_offset_x", this) / 10f, -0.495f, 0.495f);
        double offsetZ = Math.clamp(intData(behavior, "position_offset_z", this) / 10f, -0.495f, 0.495f);
        if (getBlockPos().equals(goalPos)) {
            boolean headRotation = boolData(behavior, "rotate_head", this);
            boolean spawnRot = boolData(behavior, "spawn_rot", this);
            int yaw = spawnRot ? ((IEntityDataSaver) this).getPersistentData().getInt("spawnRot") : intData(behavior, "rotation", this);
            int pitch = headRotation ? intData(behavior, "head_pitch", this) : 0;

            setHeadYaw(yaw);
            setBodyYaw(yaw);
            setYaw(yaw);
            if(headRotation){
                setPitch(pitch);
            }
        } else {
            if(!getWorld().isClient()) {
                boolean teleport = boolData(behavior, "teleport", this);
                boolean aggressive = boolData(behavior, "aggressive", this);

                if ((getNavigation().getCurrentPath() == null || getNavigation().getCurrentPath().getLastNode() == null || getNavigation().getCurrentPath().getLastNode().getBlockPos() != goalPos) && getTarget() == null) {
                    if (teleport && !aggressive) {
                        setPosition(goalPos.getX() + 0.5f + offsetX, getY(), goalPos.getZ() + 0.5f + offsetX);
                        getNavigation().stop();
                    } else {
                        getNavigation().startMovingTo(goalPos.getX() + 0.5f, goalPos.getY(), goalPos.getZ() + 0.5f, getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED) * 5);
                    }
                } if (getNavigation().getCurrentPath() != null && (getNavigation().getCurrentPath().isFinished() || getNavigation().isIdle()) && getTarget() == null) {
                    setPosition(goalPos.getX() + 0.5f + offsetX, getY(), goalPos.getZ() + 0.5f + offsetZ);
                    getNavigation().stop();
                }
            }
        }
    }

    @Override
    public float lerpYaw(float delta) {
        return getBodyYaw();
    }

    public void crawlTick(){
        this.crawling = ((IEntityDataSaver)this).getPersistentData().getBoolean("Crawling");

        calculateDimensions();
        calculateBoundingBox();

        if(!this.getWorld().isClient()) {
            BlockPos frontBlock = this.getBlockPos().offset(Direction.fromRotation(this.getHeadYaw()));
            BlockState frontState = this.getWorld().getBlockState(frontBlock);
            BlockState frontUpState = this.getWorld().getBlockState(frontBlock.up());
            BlockState upState = this.getWorld().getBlockState(this.getBlockPos().up());
            boolean shouldCrawl = (this.getNavigation().getCurrentPath() != null && (frontState.getCollisionShape(getWorld(), frontBlock).isEmpty() && !frontUpState.getCollisionShape(getWorld(), frontBlock.up()).isEmpty())) || !upState.getCollisionShape(getWorld(), this.getBlockPos().up()).isEmpty();

            boolean canSwitch = !crawling && shouldCrawl || crawling && !shouldCrawl;
            if(canSwitch){
                crawlingCooldown = Math.max(0, crawlingCooldown - 1);

                if (crawlingCooldown == 0) {
                    setCrawling(shouldCrawl);
                }
            }
            else if(crawling) {
                crawlingCooldown = 20;
            }
        }
    }

    public void runTick(){
        this.running = ((IEntityDataSaver)this).getPersistentData().getBoolean("Running");
        runningCooldown = Math.max(0, runningCooldown - 1);
        if(!this.getWorld().isClient()) {
            boolean shouldRun = (this.getNavigation().getCurrentPath() != null && this.getTarget() != null);
            if (runningCooldown == 0 && (!running && shouldRun || running && !shouldRun)) {
                setRunning(shouldRun);
                runningCooldown = 10;
            }
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public void setCrawling(boolean crawling) {
        ((IEntityDataSaver) this).getPersistentData().putBoolean("Crawling", crawling);
        NbtCompound nbt = new NbtCompound();
        nbt.put("data", ((IEntityDataSaver) this).getPersistentData().copy());
        nbt.putInt("entityID", this.getId());
        for (ServerPlayerEntity p : PlayerLookup.tracking(this)) {
            ServerPlayNetworking.send(p, new SetNbtS2CPayload(nbt, PayloadDef.ENTITY_DATA));
        }
    }
    public void setRunning(boolean running) {
        ((IEntityDataSaver) this).getPersistentData().putBoolean("Running", running);
        NbtCompound nbt = new NbtCompound();
        nbt.put("data", ((IEntityDataSaver) this).getPersistentData().copy());
        nbt.putInt("entityID", this.getId());
        for (ServerPlayerEntity p : PlayerLookup.tracking(this)) {
            ServerPlayNetworking.send(p, new SetNbtS2CPayload(nbt, PayloadDef.ENTITY_DATA));
        }
    }
    public void setDeactivated(boolean deactivated) {
        ((IEntityDataSaver) this).getPersistentData().putBoolean("Deactivated", deactivated);
        NbtCompound nbt = new NbtCompound();
        nbt.put("data", ((IEntityDataSaver) this).getPersistentData().copy());
        nbt.putInt("entityID", this.getId());
        for (ServerPlayerEntity p : PlayerLookup.tracking(this)) {
            ServerPlayNetworking.send(p, new SetNbtS2CPayload(nbt, PayloadDef.ENTITY_DATA));
        }
    }

    public boolean getCrawling(){
        return ((IEntityDataSaver)this).getPersistentData().getBoolean("Crawling");
    }
    public boolean getRunning(){
        return ((IEntityDataSaver)this).getPersistentData().getBoolean("Running");
    }

    @Override
    public EntityDimensions getBaseDimensions(EntityPose pose) {
        EntityDimensions entityDimensions = super.getBaseDimensions(pose);
        if (this.getCrawling()) {
            return EntityDimensions.fixed(entityDimensions.width(), crawlHeight()).withEyeHeight(0.5f);
        }
        return entityDimensions;
    }
    @Override
    public double getTick(Object entity) {
        if(this.menuTick)
            return MinecraftClient.getInstance().world.getTime();
        else
            return ((Entity)entity).age;
    }

    public List<?> getDataList(String string){
        return switch (string){
            default -> null;
            case "statue.animation" -> getStatueAnimations();
            case "moving.idle_animation" -> getIdleAnimations();
            case "moving.walking_animation" -> getWalkingAnimations();
            case "moving.running_animation" -> getRunningAnimations();
        };
    }

    @Override
    public ActionResult interactAt(PlayerEntity player, Vec3d hitPos, Hand hand) {
        ItemStack stack = player.getMainHandStack();
        if (stack.isOf(ItemInit.FLOPPYDISK)) {
            if (!getDisk(this, getWorld()).isEmpty()) {
                dropStack(getDisk(this, getWorld()), (float) hitPos.y);
            }
            ItemStack disk = player.getMainHandStack();
            putFloppyDisk(this, disk, getWorld());
            disk.decrementUnlessCreative(1, player);
            return ActionResult.SUCCESS;
        } else if (player.getMainHandStack().isOf(ItemInit.WRENCH)) {
            ItemStack disk = getDisk(this, getWorld());
            if (!disk.isEmpty()) {
                dropStack(disk, (float) hitPos.y);
                putFloppyDisk(this, ItemStack.EMPTY, getWorld());
                return ActionResult.SUCCESS;
            }
        } else if (player.getMainHandStack().isOf(ItemInit.PAINTBRUSH)) {
            String skin = ((IEntityDataSaver) this).getPersistentData().getString("Reskin");
            if (Objects.equals(skin, "neon")) ((IEntityDataSaver) this).getPersistentData().putString("Reskin", "");
            else ((IEntityDataSaver) this).getPersistentData().putString("Reskin", "neon");
            return ActionResult.SUCCESS;
        }
        return super.interactAt(player, hitPos, hand);
    }

    public String getBehavior() {
        return this.behavior;
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        ItemStack disk = getDisk(this, getWorld());
        if (!disk.isEmpty()) {
            dropStack(disk);
            putFloppyDisk(this, ItemStack.EMPTY, getWorld());
        }
        ItemStack spawnItem = getPickBlockStack();

        dropStack(spawnItem);
        super.onDeath(damageSource);
    }

    @Nullable
    @Override
    public ItemStack getPickBlockStack() {
        EntitySpawnItem spawnItem = EntitySpawnItem.forEntity(this.getType());
        if (spawnItem == null) {
            return super.getPickBlockStack();
        }
        ItemStack stack = new ItemStack(spawnItem);
        ItemNbtUtil.setNbt(stack, ((IEntityDataSaver)this).getPersistentData());
        return stack;
    }

    @Override
    protected void updatePostDeath() {
        this.deathTime++;
        if (this.deathTime >= deathLength() && !this.getWorld().isClient() && !this.isRemoved()) {
            this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_DEATH_PARTICLES);
            this.remove(Entity.RemovalReason.KILLED);
        }
    }

    void putFloppyDisk(PathAwareEntity entity, ItemStack disk, World world){
        ((IEntityDataSaver)entity).getPersistentData().put("floppy_disk", disk.encodeAllowEmpty(world.getRegistryManager()));
        if(world.isClient()){
            ScreenUtils.saveNbtFromScreen(((IEntityDataSaver)entity).getPersistentData(), entity.getId());
        }
    }
    ItemStack getDisk(PathAwareEntity entity, World world){
        return ItemStack.fromNbtOrEmpty(world.getRegistryManager(), ((IEntityDataSaver)entity).getPersistentData().getCompound("floppy_disk"));
    }
    String getAIMode(PathAwareEntity entity, World world){
        NbtCompound diskData = ItemNbtUtil.getNbt(getDisk(entity, world));
        if(diskData.isEmpty()) return "";

        NbtCompound data = diskData.getCompound("" + getAIHour(world));
        if(data.isEmpty()){
            int i = getAIHour(world);
            while(diskData.getCompound("" + i).getString("Behavior").isEmpty()){
                i--;
            }
            return diskData.getCompound("" + i).getString("Behavior");
        }
        else {
            return data.getString("Behavior");
        }
    }
    public int getAIHour(World world){
        long time = world.getTimeOfDay() * 4;

        long hour = (time / 1000) - ((96000 * (time / 96000)) / 1000);
        int realHour = (int)hour + 24;
        realHour = realHour >= 96 ? realHour - 96 : realHour;
        return realHour;
    }
    public NbtCompound getAIData(PathAwareEntity entity, World world){
        NbtCompound diskData = ItemNbtUtil.getNbt(getDisk(entity, world));
        if(diskData.isEmpty()) return new NbtCompound();
        NbtCompound data = diskData.getCompound("" + getAIHour(world));
        if(data.isEmpty()){
            int i = getAIHour(world);
            while(diskData.getCompound("" + i).getString("Behavior").isEmpty()){
                i--;
            }
            return diskData.getCompound("" + i);
        }
        else {
            return data;
        }
    }
    public boolean boolData(String behavior, String option, PathAwareEntity entity) {
        int index = getIndex(behavior, option);
        return getAIData(entity, entity.getWorld()).getBoolean("" + index);
    }
    public int intData(String behavior, String option, PathAwareEntity entity) {
        int index = getIndex(behavior, option);
        return getAIData(entity, entity.getWorld()).getInt("" + index);
    }
    public BlockPos blockPosData(String behavior, String option, PathAwareEntity entity) {
        int index = getIndex(behavior, option);
        return BlockPos.fromLong(getAIData(entity, entity.getWorld()).getLong("" + index));
    }
    public String stringData(String behavior, String option, PathAwareEntity entity) {
        int index = getIndex(behavior, option);
        return getAIData(entity, entity.getWorld()).getString("" + index);
    }
    private int getIndex(String behavior, String option){
        if(ComputerData.getAIBehavior(behavior) instanceof ComputerAI ai) {
            return ai.getOptionIndex(option);
        }
        return -1;
    }

    float crawlHeight(){
        EntityDataManager manager = ((IGetClientManagers) MinecraftClient.getInstance()).getEntityDataManager();
        return manager.getEntityData(getType()).crawl_height();
    }
    boolean canCrawl(){
        EntityDataManager manager = ((IGetClientManagers) MinecraftClient.getInstance()).getEntityDataManager();
        return manager.getEntityData(getType()).can_crawl();

    }

    List<String> getStatueAnimations(){
        List<String> list = new ArrayList<>();
        list.add(defaultIdleAnim());
        list.add(defaultWalkingAnim());
        list.add(defaultRunningAnim());
        list.add(performingAnim());
        list.add(deactivatedAnim());
        list.add(aggressiveIdleAnim());
        list.add(aggressiveWalkingAnim());
        list.add(aggressiveRunningAnim());
        list.add(crawlingIdleAnim());
        list.add(crawlingWalkingAnim());
        list.add(attackAnim());
        return list;
    }
    List<String> getIdleAnimations(){
        List<String> list = new ArrayList<>();
        list.add(defaultIdleAnim());
        list.add(aggressiveIdleAnim());
        list.add(crawlingIdleAnim());
        list.add(deactivatedAnim());
        list.add(performingAnim());
        return list;
    };
    List<String> getWalkingAnimations(){
        List<String> list = new ArrayList<>();
        list.add(defaultWalkingAnim());
        list.add(aggressiveWalkingAnim());
        list.add(crawlingWalkingAnim());
        return list;
    }
    List<String> getRunningAnimations(){
        List<String> list = new ArrayList<>();
        list.add(defaultRunningAnim());
        list.add(aggressiveRunningAnim());
        return list;
    }

    public String demoAnim() {
        return defaultIdleAnim();
    }
    protected abstract String defaultIdleAnim();
    protected abstract String defaultWalkingAnim();
    protected abstract String defaultRunningAnim();
    protected abstract String performingAnim();
    protected abstract String deactivatingAnim();
    protected abstract String deactivatedAnim();
    protected abstract String activatingAnim();
    protected abstract String aggressiveIdleAnim();
    protected abstract String aggressiveWalkingAnim();
    protected abstract String aggressiveRunningAnim();
    protected abstract String crawlingIdleAnim();
    protected abstract String crawlingWalkingAnim();
    protected abstract String JumpScareAnim();
    protected abstract String attackAnim();
    protected abstract String dyingAnim();
    protected abstract int deathLength();
}
