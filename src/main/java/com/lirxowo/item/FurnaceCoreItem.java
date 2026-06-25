package com.lirxowo.item;

import com.lirxowo.Whispering;
import com.lirxowo.compat.WPCompat;
import io.wispforest.accessories.api.AccessoryItem;
import io.wispforest.accessories.api.attributes.AccessoryAttributeBuilder;
import io.wispforest.accessories.api.slot.SlotReference;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FurnaceCoreItem extends AccessoryItem {

    private static final ResourceLocation DAMAGE_MODIFIER_ID = new ResourceLocation(Whispering.MOD_ID, "furnace_core");
    private static final double DAMAGE_BONUS = 0.10D;

    private static final ResourceLocation KNOCKBACK_MODIFIER_ID = new ResourceLocation(Whispering.MOD_ID, "furnace_core_knockback");
    private static final double KNOCKBACK_IMMUNITY = 1.0D;

    private static final int EFFECT_INTERVAL_TICKS = 20;
    private static final int EFFECT_DURATION_TICKS = 60;
    private static final int EFFECT_AMPLIFIER = 1;
    private static final int UPKEEP_INTERVAL_TICKS = 5 * 60 * 20;

    public FurnaceCoreItem(Properties properties) {
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
        builder.addStackable(Attributes.KNOCKBACK_RESISTANCE, KNOCKBACK_MODIFIER_ID,
                KNOCKBACK_IMMUNITY, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public void tick(ItemStack stack, SlotReference reference) {
        if (!(reference.entity() instanceof Player player) || player.level().isClientSide()) {
            return;
        }
        Item fuel = WPCompat.item(WPCompat.ITEM_LAVA_POWER_CELL);
        if (fuel == null || !hasItem(player, fuel)) {
            return;
        }
        if (player.tickCount % EFFECT_INTERVAL_TICKS == 0) {
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, EFFECT_DURATION_TICKS, EFFECT_AMPLIFIER, false, true));
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, EFFECT_DURATION_TICKS, EFFECT_AMPLIFIER, false, true));
            WPCompat.applyEffect(player, WPCompat.EFFECT_NOURISHMENT, EFFECT_DURATION_TICKS, 0);
        }
        if (player.tickCount % UPKEEP_INTERVAL_TICKS == 0) {
            consumeItem(player, fuel);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.whispering.furnace_core.tooltip.fuel"));
        tooltip.add(Component.translatable("item.whispering.furnace_core.tooltip.knockback"));
        tooltip.add(Component.translatable("item.whispering.furnace_core.tooltip.damage"));
    }

    private static boolean hasItem(Player player, Item item) {
        Inventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            if (inventory.getItem(i).is(item)) {
                return true;
            }
        }
        return false;
    }

    private static void consumeItem(Player player, Item item) {
        Inventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack slot = inventory.getItem(i);
            if (slot.is(item)) {
                slot.shrink(1);
                return;
            }
        }
    }
}
