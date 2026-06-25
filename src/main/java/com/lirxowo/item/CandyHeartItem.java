package com.lirxowo.item;

import com.lirxowo.Whispering;
import io.wispforest.accessories.api.AccessoryItem;
import io.wispforest.accessories.api.attributes.AccessoryAttributeBuilder;
import io.wispforest.accessories.api.slot.SlotReference;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CandyHeartItem extends AccessoryItem {

    private static final ResourceLocation HEALTH_MODIFIER_ID = new ResourceLocation(Whispering.MOD_ID, "candy_heart");
    private static final double MAX_HEALTH_BONUS = 10.0D;

    public CandyHeartItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canEquip(ItemStack stack, SlotReference reference) {
        return AccessoryHelper.BADGE_SLOT.equals(reference.slotName());
    }

    @Override
    public void getDynamicModifiers(ItemStack stack, SlotReference reference, AccessoryAttributeBuilder builder) {
        if (AccessoryHelper.BADGE_SLOT.equals(reference.slotName())) {
            builder.addStackable(Attributes.MAX_HEALTH, HEALTH_MODIFIER_ID, MAX_HEALTH_BONUS, AttributeModifier.Operation.ADDITION);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.whispering.candy_heart.tooltip.health"));
        tooltip.add(Component.translatable("item.whispering.candy_heart.tooltip.drop"));
    }
}
