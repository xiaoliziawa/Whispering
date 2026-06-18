package com.lirxowo.item;

import com.lirxowo.Whispering;
import com.lirxowo.item.reg.AllCurios;
import com.lirxowo.network.Network;
import io.wispforest.accessories.api.AccessoryItem;
import io.wispforest.accessories.api.attributes.AccessoryAttributeBuilder;
import io.wispforest.accessories.api.slot.SlotReference;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LavaHeartShieldItem extends AccessoryItem {

    private static final ResourceLocation DAMAGE_MODIFIER_ID = new ResourceLocation(Whispering.MOD_ID, "lava_heart_shield");
    private static final double DAMAGE_BONUS = 0.10D;

    public static final int COOLDOWN_TICKS = 3 * 60 * 20;

    private static final int REGENERATION_DURATION = 900;
    private static final int ABSORPTION_DURATION = 100;
    private static final int FIRE_RESISTANCE_DURATION = 800;

    public LavaHeartShieldItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canEquip(ItemStack stack, SlotReference reference) {
        return AccessoryHelper.BADGE_SLOT.equals(reference.slotName());
    }

    @Override
    public void getDynamicModifiers(ItemStack stack, SlotReference reference, AccessoryAttributeBuilder builder) {
        if (!AccessoryHelper.BADGE_SLOT.equals(reference.slotName())) {
            return;
        }
        AccessoryHelper.applyAllDamageBonus(builder, DAMAGE_MODIFIER_ID, DAMAGE_BONUS);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.whispering.lava_heart_shield.tooltip.totem"));
        tooltip.add(Component.translatable("item.whispering.lava_heart_shield.tooltip.damage"));
        tooltip.add(Component.translatable("item.whispering.lava_heart_shield.tooltip.cooldown"));
    }

    public static void activate(ServerPlayer player) {
        player.setHealth(1.0F);
        player.removeAllEffects();
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, REGENERATION_DURATION, 1));
        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, ABSORPTION_DURATION, 1));
        player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, FIRE_RESISTANCE_DURATION, 0));
        player.getCooldowns().addCooldown(AllCurios.LAVA_HEART_SHIELD, COOLDOWN_TICKS);
        Network.sendTotemAnimation(player);
    }
}
