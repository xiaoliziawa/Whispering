package com.lirxowo.item;

import com.lirxowo.Whispering;
import io.wispforest.accessories.api.AccessoryItem;
import io.wispforest.accessories.api.attributes.AccessoryAttributeBuilder;
import io.wispforest.accessories.api.slot.SlotReference;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SculkBladeItem extends AccessoryItem {

    private static final ResourceLocation DAMAGE_MODIFIER_ID = new ResourceLocation(Whispering.MOD_ID, "sculk_blade");
    private static final double DAMAGE_BONUS = 0.10D;

    public static final float KILL_HEAL_FRACTION = 0.02F;
    public static final float EXECUTE_HEALTH_FRACTION = 0.15F;
    public static final float EXECUTE_CHANCE = 0.02F;

    public SculkBladeItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canEquip(ItemStack stack, SlotReference reference) {
        return AccessoryHelper.BADGE_SLOT.equals(reference.slotName());
    }

    @Override
    public void getDynamicModifiers(ItemStack stack, SlotReference reference, AccessoryAttributeBuilder builder) {
        if (AccessoryHelper.BADGE_SLOT.equals(reference.slotName())) {
            AccessoryHelper.applyAllDamageBonus(builder, DAMAGE_MODIFIER_ID, DAMAGE_BONUS);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.whispering.sculk_blade.tooltip.lifesteal"));
        tooltip.add(Component.translatable("item.whispering.sculk_blade.tooltip.execute"));
        tooltip.add(Component.translatable("item.whispering.sculk_blade.tooltip.damage"));
    }
}
