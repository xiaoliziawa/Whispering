package com.lirxowo.item;

import com.lirxowo.compat.WPCompat;
import com.lirxowo.item.reg.AllCurios;
import io.wispforest.accessories.api.AccessoryItem;
import io.wispforest.accessories.api.slot.SlotReference;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AbyssalHeartItem extends AccessoryItem {

    public static final float DAMAGE_IMMUNE_CHANCE = 0.08F;
    public static final float PROJECTILE_DAMAGE_MULTIPLIER = 0.80F;

    private static final int MAX_STACKS = 10;
    private static final int STACK_DURATION_TICKS = 200;
    private static final double BONUS_PER_STACK = 0.02D;

    private static final UUID FRENZY_MODIFIER_UUID = UUID.fromString("7f3e2a10-9c44-4b8e-a1d6-2e5b3c9f01aa");
    private static final String FRENZY_MODIFIER_NAME = "whispering_abyssal_frenzy";

    private record FrenzyState(int stacks, int expiryTick) {
    }

    private static final Map<UUID, FrenzyState> STATES = new ConcurrentHashMap<>();

    public AbyssalHeartItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canEquip(ItemStack stack, SlotReference reference) {
        return AccessoryHelper.BADGE_SLOT.equals(reference.slotName());
    }

    @Override
    public void tick(ItemStack stack, SlotReference reference) {
        if (!(reference.entity() instanceof Player player) || player.level().isClientSide()) {
            return;
        }
        FrenzyState state = STATES.get(player.getUUID());
        if (state != null && player.tickCount >= state.expiryTick()) {
            clearModifiers(player);
            STATES.remove(player.getUUID());
        }
    }

    @Override
    public void onUnequip(ItemStack stack, SlotReference reference) {
        if (reference.entity() instanceof Player player && !player.level().isClientSide()) {
            clearModifiers(player);
            STATES.remove(player.getUUID());
        }
    }

    public static void onKill(Player player) {
        if (!AccessoryHelper.isWorn(player, AllCurios.ABYSSAL_HEART)) {
            return;
        }
        FrenzyState previous = STATES.get(player.getUUID());
        int stacks = Math.min((previous == null ? 0 : previous.stacks()) + 1, MAX_STACKS);
        STATES.put(player.getUUID(), new FrenzyState(stacks, player.tickCount + STACK_DURATION_TICKS));
        applyModifiers(player, stacks);
    }

    private static void applyModifiers(Player player, int stacks) {
        double value = BONUS_PER_STACK * stacks;
        for (ResourceLocation id : WPCompat.SPELL_SCHOOL_ATTRIBUTES) {
            AttributeInstance instance = resolve(player, id);
            if (instance == null) {
                continue;
            }
            instance.removeModifier(FRENZY_MODIFIER_UUID);
            instance.addTransientModifier(new AttributeModifier(
                    FRENZY_MODIFIER_UUID, FRENZY_MODIFIER_NAME, value, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
    }

    private static void clearModifiers(Player player) {
        for (ResourceLocation id : WPCompat.SPELL_SCHOOL_ATTRIBUTES) {
            AttributeInstance instance = resolve(player, id);
            if (instance != null) {
                instance.removeModifier(FRENZY_MODIFIER_UUID);
            }
        }
    }

    @Nullable
    private static AttributeInstance resolve(Player player, ResourceLocation id) {
        Attribute attribute = WPCompat.attribute(id);
        return attribute == null ? null : player.getAttribute(attribute);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.whispering.abyssal_heart.tooltip.immune"));
        tooltip.add(Component.translatable("item.whispering.abyssal_heart.tooltip.projectile"));
        tooltip.add(Component.translatable("item.whispering.abyssal_heart.tooltip.frenzy"));
    }
}
