package net.zephyr.goopyutil.entity.base;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.zephyr.goopyutil.blocks.computer.ComputerData;
import net.zephyr.goopyutil.networking.PayloadDef;
import net.zephyr.goopyutil.networking.payloads.SetNbtS2CPayload;
import net.zephyr.goopyutil.util.Computer.ComputerAI;
import net.zephyr.goopyutil.util.IEntityDataSaver;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.*;

public abstract class GoopyGeckoEntity extends PathAwareEntity implements GeoEntity, GoopyEntity {
    private Map<String, Identifier[]> Reskins = new HashMap<>();
    private AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    protected static RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("animation.zephyr.dayidle");
    protected static RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("animation.zephyr.daywalk");
    protected static RawAnimation CRAWL_IDLE_ANIM = RawAnimation.begin().thenLoop("animation.zephyr.crawlidle");
    protected static RawAnimation CRAWL_WALK_ANIM = RawAnimation.begin().thenLoop("animation.zephyr.crawling");

    boolean crawling = false;
    int crawlingCooldown = 0;
    public boolean menuTick = false;

    public GoopyGeckoEntity(EntityType<? extends PathAwareEntity> type, World world) {
        super(type, world);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Movement", 3, this::movementAnimController)
                .triggerableAnim(demoAnim().toString(), demoAnim()));
    }

    public <E extends GoopyGeckoEntity> PlayState movementAnimController(final AnimationState<E> event) {
        if(this.crawling){
            if (event.isMoving())
                return event.setAndContinue(CRAWL_WALK_ANIM);
            else
                return event.setAndContinue(CRAWL_IDLE_ANIM);
        }
        else {
            if (event.isMoving())
                return event.setAndContinue(WALK_ANIM);
            else
                return event.setAndContinue(IDLE_ANIM);
        }
        //return PlayState.STOP;
    }

    @Override
    public void tick() {
        super.tick();
        if(canCrawl()){
            crawlTick();
        }
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

    public boolean getCrawling(){
        return ((IEntityDataSaver)this).getPersistentData().getBoolean("Crawling");
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
        };
    }
}
