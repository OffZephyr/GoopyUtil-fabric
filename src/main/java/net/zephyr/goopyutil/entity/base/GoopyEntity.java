package net.zephyr.goopyutil.entity.base;

import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.goopyutil.blocks.computer.ComputerData;
import net.zephyr.goopyutil.util.Computer.ComputerAI;
import net.zephyr.goopyutil.util.GoopyScreens;
import net.zephyr.goopyutil.util.mixinAccessing.IEntityDataSaver;
import net.zephyr.goopyutil.util.ItemNbtUtil;

import java.util.ArrayList;
import java.util.List;

public interface GoopyEntity {
    boolean canCrawl();
    float crawlHeight();
    String demoAnim();
    default List<String> getStatueAnimations(){
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
    default List<String> getIdleAnimations(){
        List<String> list = new ArrayList<>();
        list.add(defaultIdleAnim());
        list.add(aggressiveIdleAnim());
        list.add(crawlingIdleAnim());
        list.add(deactivatedAnim());
        list.add(performingAnim());
        return list;
    };
    default List<String> getWalkingAnimations(){
        List<String> list = new ArrayList<>();
        list.add(defaultWalkingAnim());
        list.add(aggressiveWalkingAnim());
        list.add(crawlingWalkingAnim());
        return list;
    }
    default List<String> getRunningAnimations(){
        List<String> list = new ArrayList<>();
        list.add(defaultRunningAnim());
        list.add(aggressiveRunningAnim());
        return list;
    }

    String defaultIdleAnim();
    String defaultWalkingAnim();
    String defaultRunningAnim();
    String performingAnim();
    String deactivatingAnim();
    String deactivatedAnim();
    String activatingAnim();
    String aggressiveIdleAnim();
    String aggressiveWalkingAnim();
    String aggressiveRunningAnim();
    String crawlingIdleAnim();
    String crawlingWalkingAnim();
    String JumpScareAnim();
    String attackAnim();
    String dyingAnim();
    int deathLength();
    default void putFloppyDisk(PathAwareEntity entity, ItemStack disk, World world){
        ((IEntityDataSaver)entity).getPersistentData().put("floppy_disk", disk.encodeAllowEmpty(world.getRegistryManager()));
        if(world.isClient()){
            GoopyScreens.saveNbtFromScreen(((IEntityDataSaver)entity).getPersistentData(), entity.getId());
        }
    }
    default ItemStack getDisk(PathAwareEntity entity, World world){
        return ItemStack.fromNbtOrEmpty(world.getRegistryManager(), ((IEntityDataSaver)entity).getPersistentData().getCompound("floppy_disk"));
    }
    default String getAIMode(PathAwareEntity entity, World world){
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
    default int getAIHour(World world){
        long time = world.getTimeOfDay() * 4;

        long hour = (time / 1000) - ((96000 * (time / 96000)) / 1000);
        int realHour = (int)hour + 24;
        realHour = realHour >= 96 ? realHour - 96 : realHour;
        return realHour;
    }
    default NbtCompound getAIData(PathAwareEntity entity, World world){
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


    default boolean boolData(String behavior, String option, PathAwareEntity entity) {
        int index = getIndex(behavior, option);
        return getAIData(entity, entity.getWorld()).getBoolean("" + index);
    }
    default int intData(String behavior, String option, PathAwareEntity entity) {
        int index = getIndex(behavior, option);
        return getAIData(entity, entity.getWorld()).getInt("" + index);
    }
    default BlockPos blockPosData(String behavior, String option, PathAwareEntity entity) {
        int index = getIndex(behavior, option);
        return BlockPos.fromLong(getAIData(entity, entity.getWorld()).getLong("" + index));
    }
    default String stringData(String behavior, String option, PathAwareEntity entity) {
        int index = getIndex(behavior, option);
        return getAIData(entity, entity.getWorld()).getString("" + index);
    }
    private int getIndex(String behavior, String option){
        if(ComputerData.getAIBehavior(behavior) instanceof ComputerAI ai) {
            return ai.getOptionIndex(option);
        }
        return -1;
    }
}
