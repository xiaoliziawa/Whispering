package com.lirxowo.item;

import com.lirxowo.Whispering;
import com.lirxowo.item.reg.AllCurios;
import io.wispforest.accessories.api.AccessoryItem;
import io.wispforest.accessories.api.attributes.AccessoryAttributeBuilder;
import io.wispforest.accessories.api.slot.SlotReference;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LavaWingsItem extends AccessoryItem {

    private static final ResourceLocation DAMAGE_MODIFIER_ID = new ResourceLocation(Whispering.MOD_ID, "lava_wings");
    private static final double DAMAGE_BONUS = 0.10D;

    public static final int COOLDOWN_TICKS = 2 * 60 * 20;

    private static final float TRIGGER_HEALTH_FRACTION = 0.15F;
    private static final float HEAL_FRACTION = 0.30F;
    private static final int INVINCIBLE_TICKS = 100;
    private static final int RESISTANCE_AMPLIFIER = 4;

    private static final Map<UUID, Integer> SCHEDULED_HEAL = new ConcurrentHashMap<>();

    public LavaWingsItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canEquip(ItemStack stack, SlotReference reference) {
        return AccessoryHelper.BACK_SLOT.equals(reference.slotName());
    }

    @Override
    public void getDynamicModifiers(ItemStack stack, SlotReference reference, AccessoryAttributeBuilder builder) {
        if (AccessoryHelper.BACK_SLOT.equals(reference.slotName())) {
            AccessoryHelper.applyAllDamageBonus(builder, DAMAGE_MODIFIER_ID, DAMAGE_BONUS);
        }
    }

    @Override
    public void tick(ItemStack stack, SlotReference reference) {
        if (!(reference.entity() instanceof Player player) || player.level().isClientSide()) {
            return;
        }
        grantFlight(player);
        handleGuardianAngel(player);
    }

    @Override
    public void onUnequip(ItemStack stack, SlotReference reference) {
        if (reference.entity() instanceof Player player) {
            SCHEDULED_HEAL.remove(player.getUUID());
            revokeFlight(player);
        }
    }

    private void grantFlight(Player player) {
        Abilities abilities = player.getAbilities();
        if (!abilities.mayfly) {
            abilities.mayfly = true;
            player.onUpdateAbilities();
        }
    }

    private void revokeFlight(Player player) {
        if (player.isCreative() || player.isSpectator()) {
            return;
        }
        Abilities abilities = player.getAbilities();
        if (abilities.mayfly) {
            abilities.mayfly = false;
            abilities.flying = false;
            player.onUpdateAbilities();
        }
    }

    private void handleGuardianAngel(Player player) {
        UUID id = player.getUUID();
        Integer healAt = SCHEDULED_HEAL.get(id);
        if (healAt != null) {
            if (player.tickCount >= healAt) {
                player.heal(player.getMaxHealth() * HEAL_FRACTION);
                SCHEDULED_HEAL.remove(id);
            }
            return;
        }
        if (player.getCooldowns().isOnCooldown(AllCurios.LAVA_WINGS)) {
            return;
        }
        if (player.isAlive() && player.getHealth() < player.getMaxHealth() * TRIGGER_HEALTH_FRACTION) {
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, INVINCIBLE_TICKS, RESISTANCE_AMPLIFIER, false, true));
            player.getCooldowns().addCooldown(AllCurios.LAVA_WINGS, COOLDOWN_TICKS);
            SCHEDULED_HEAL.put(id, player.tickCount + INVINCIBLE_TICKS);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.whispering.lava_wings.tooltip.flight"));
        tooltip.add(Component.translatable("item.whispering.lava_wings.tooltip.guardian"));
        tooltip.add(Component.translatable("item.whispering.lava_wings.tooltip.damage"));
        tooltip.add(Component.translatable("item.whispering.lava_wings.tooltip.cooldown"));
    }
}
