package net.zephyr.goopyutil.item;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Equipment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.zephyr.goopyutil.blocks.computer.ComputerData;
import net.zephyr.goopyutil.entity.base.GoopyUtilEntity;
import net.zephyr.goopyutil.util.ItemNbtUtil;
import net.zephyr.goopyutil.util.mixinAccessing.IEntityDataSaver;

import java.util.List;

public class IllusionDisc extends ItemWithDescription implements Equipment {
    public IllusionDisc(Settings settings, int... tools) {
        super(settings, ItemWithDescription.COIN, ItemWithDescription.COMPUTER);
    }

    @Override
    public EquipmentSlot getSlotType() {
        return EquipmentSlot.CHEST;
    }

    @Override
    public RegistryEntry<SoundEvent> getEquipSound() {
        return Equipment.super.getEquipSound();
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) throws NumberFormatException {
        String animatronic = ItemNbtUtil.getNbt(stack).getString("entity");
        if (!animatronic.isEmpty() && ComputerData.getAIAnimatronic(animatronic) instanceof ComputerData.Initializer.AnimatronicAI ai) {
            Text before = Text.translatable("item.goopyutil.floppy_disk.entity_name", "§7" + ai.entityType().getName().getString());
            Text text = Text.literal(before.getString());
            tooltip.add(text);
        }
        super.appendTooltip(stack, context, tooltip, type);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if(entity instanceof GoopyUtilEntity ent) {
            for (ComputerData.Initializer.AnimatronicAI ai: ComputerData.getAIAnimatronics())
            {
                if(ai.entityType() == entity.getType()) {
                    String entityID = ai.id();
                    NbtCompound nbt = ItemNbtUtil.getNbt(stack);
                    nbt.putString("entity", entityID);
                    nbt.put("entityData", ((IEntityDataSaver)ent).getPersistentData());
                    ItemNbtUtil.setNbt(stack, nbt.copy());
                    ItemNbtUtil.setNbt(user.getMainHandStack(), nbt.copy());
                    user.sendMessage(Text.translatable("item.goopyutil.illusion_disc.entity_updated"), true);
                    System.out.println(ItemNbtUtil.getNbt(stack).getCompound("entityData").asString());
                    return ActionResult.SUCCESS;
                }
            }
        }
        return super.useOnEntity(stack, user, entity, hand);
    }
}
