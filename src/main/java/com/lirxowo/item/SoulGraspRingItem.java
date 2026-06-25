package com.lirxowo.item;

import com.lirxowo.Whispering;
import io.wispforest.accessories.api.AccessoryItem;
import io.wispforest.accessories.api.attributes.AccessoryAttributeBuilder;
import io.wispforest.accessories.api.slot.SlotReference;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SoulGraspRingItem extends AccessoryItem {

    private static final ResourceLocation DAMAGE_MODIFIER_ID = new ResourceLocation(Whispering.MOD_ID, "soul_grasp_ring");
    private static final double DAMAGE_BONUS = 0.08D;

    public static final float STICK_DROP_CHANCE = 0.10F;

    private static final int NIGHT_VISION_INTERVAL_TICKS = 20;
    private static final int NIGHT_VISION_DURATION_TICKS = 400;

    public SoulGraspRingItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canEquip(ItemStack stack, SlotReference reference) {
        return AccessoryHelper.RING_SLOT.equals(reference.slotName());
    }

    @Override
    public void getDynamicModifiers(ItemStack stack, SlotReference reference, AccessoryAttributeBuilder builder) {
        if (AccessoryHelper.RING_SLOT.equals(reference.slotName())) {
            AccessoryHelper.applyAllDamageBonus(builder, DAMAGE_MODIFIER_ID, DAMAGE_BONUS);
        }
    }

    @Override
    public void tick(ItemStack stack, SlotReference reference) {
        if (!(reference.entity() instanceof Player player) || player.level().isClientSide()) {
            return;
        }
        if (player.tickCount % NIGHT_VISION_INTERVAL_TICKS == 0) {
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, NIGHT_VISION_DURATION_TICKS, 0, false, false, true));
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.whispering.soul_grasp_ring.tooltip.drop"));
        tooltip.add(Component.translatable("item.whispering.soul_grasp_ring.tooltip.nightvision"));
        tooltip.add(Component.translatable("item.whispering.soul_grasp_ring.tooltip.damage"));
    }
}
