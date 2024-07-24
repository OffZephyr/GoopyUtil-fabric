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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.zephyr.goopyutil.init.ItemInit;
import net.zephyr.goopyutil.networking.PayloadDef;
import net.zephyr.goopyutil.networking.payloads.SetNbtS2CPayload;
import net.zephyr.goopyutil.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.*;

public abstract class GoopyGeckoEntity extends PathAwareEntity implements GeoEntity, GoopyEntity {
    private Map<String, Identifier[]> Reskins = new HashMap<>();
    private AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    boolean running = false;
    int runningCooldown = 0;
    boolean crawling = false;
    int crawlingCooldown = 0;
    boolean deactivated = false;
    public boolean menuTick = false;
    private String behavior;

    public GoopyGeckoEntity(EntityType<? extends PathAwareEntity> type, World world) {
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

    private <E extends GoopyGeckoEntity> PlayState attackAnimController(AnimationState<E> event) {
        if(this.handSwinging && event.getController().getAnimationState().equals(AnimationController.State.STOPPED)) {
            event.getController().forceAnimationReset();
            this.handSwinging = false;
            return event.setAndContinue(RawAnimation.begin().thenPlay(attackAnim()));
        }
        return PlayState.CONTINUE;
    }

    public <E extends GoopyGeckoEntity> PlayState movementAnimController(final AnimationState<E> event) {
        if(event.getAnimatable().isDead()){
            return event.setAndContinue(RawAnimation.begin().thenPlay(dyingAnim()));
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

                if (getNavigation().getCurrentPath() == null || getNavigation().getCurrentPath().getLastNode() == null || getNavigation().getCurrentPath().getLastNode().getBlockPos() != goalPos) {
                    if (teleport) {
                        setPosition(goalPos.getX() + 0.5f + offsetX, getY(), goalPos.getZ() + 0.5f + offsetX);
                        getNavigation().stop();
                    } else {
                        getNavigation().startMovingTo(goalPos.getX() + 0.5f, goalPos.getY(), goalPos.getZ() + 0.5f, getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED) * 5);
                    }
                } if (getNavigation().getCurrentPath() != null && (getNavigation().getCurrentPath().isFinished() || getNavigation().isIdle())) {
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

        crawlingCooldown = Math.max(0, crawlingCooldown - 1);
        if(!this.getWorld().isClient()) {
            BlockPos frontBlock = this.getBlockPos().offset(this.getMovementDirection());
            BlockState frontState = this.getWorld().getBlockState(frontBlock);
            BlockState frontUpState = this.getWorld().getBlockState(frontBlock.up());
            BlockState upState = this.getWorld().getBlockState(this.getBlockPos().up());
            boolean shouldCrawl = (this.getNavigation().getCurrentPath() != null && (frontState.isAir() && !frontUpState.isAir())) || !upState.isAir();
            if (crawlingCooldown == 0 && (!crawling && shouldCrawl || crawling && !shouldCrawl)) {
                setCrawling(shouldCrawl);
                crawlingCooldown = 30;
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

    public void addReskin(String name, Identifier texture, Identifier obj, Identifier animations){
        Identifier[] skin = new Identifier[3];
        skin[0] = texture;
        skin[1] = obj;
        skin[2] = animations;

        Reskins.put(name, skin);
    }

    public Map<String, Identifier[]> getReskins(){
        return Reskins;
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
        super.onDeath(damageSource);
    }

    @Override
    protected void updatePostDeath() {
        this.deathTime++;
        if (this.deathTime >= deathLength() && !this.getWorld().isClient() && !this.isRemoved()) {
            this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_DEATH_PARTICLES);
            this.remove(Entity.RemovalReason.KILLED);
        }
    }
}
