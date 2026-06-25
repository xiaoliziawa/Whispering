package com.lirxowo.item;

import com.lirxowo.Whispering;
import com.lirxowo.item.reg.AllCurios;
import io.wispforest.accessories.api.AccessoryItem;
import io.wispforest.accessories.api.attributes.AccessoryAttributeBuilder;
import io.wispforest.accessories.api.slot.SlotReference;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WaterRuneItem extends AccessoryItem {

    private static final ResourceLocation DAMAGE_MODIFIER_ID = new ResourceLocation(Whispering.MOD_ID, "water_rune");
    private static final double DAMAGE_BONUS = 0.10D;

    public WaterRuneItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canEquip(ItemStack stack, SlotReference reference) {
        return AccessoryHelper.NECKLACE_SLOT.equals(reference.slotName());
    }

    @Override
    public void getDynamicModifiers(ItemStack stack, SlotReference reference, AccessoryAttributeBuilder builder) {
        if (AccessoryHelper.NECKLACE_SLOT.equals(reference.slotName())) {
            AccessoryHelper.applyAllDamageBonus(builder, DAMAGE_MODIFIER_ID, DAMAGE_BONUS);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.whispering.water_rune.tooltip.levitation"));
        tooltip.add(Component.translatable("item.whispering.water_rune.tooltip.fall"));
        tooltip.add(Component.translatable("item.whispering.water_rune.tooltip.jump"));
        tooltip.add(Component.translatable("item.whispering.water_rune.tooltip.damage"));
    }

    public static boolean isWorn(LivingEntity entity) {
        return AccessoryHelper.isWorn(entity, AllCurios.WATER_RUNE);
    }
}
