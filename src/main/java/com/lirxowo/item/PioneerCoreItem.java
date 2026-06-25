package com.lirxowo.item;

import com.lirxowo.Whispering;
import com.lirxowo.compat.WPCompat;
import com.lirxowo.item.reg.AllCurios;
import io.wispforest.accessories.api.AccessoryItem;
import io.wispforest.accessories.api.attributes.AccessoryAttributeBuilder;
import io.wispforest.accessories.api.slot.SlotReference;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PioneerCoreItem extends AccessoryItem {

    private static final ResourceLocation DAMAGE_MODIFIER_ID = new ResourceLocation(Whispering.MOD_ID, "pioneer_core");
    private static final double DAMAGE_BONUS = 0.10D;

    public static final int FUEL_DRAIN_INTERVAL_TICKS = 240;

    public PioneerCoreItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canEquip(ItemStack stack, SlotReference reference) {
        return AccessoryHelper.BADGE_SLOT.equals(reference.slotName());
    }

    @Override
    public void tick(ItemStack stack, SlotReference reference) {
        if (reference.entity() instanceof Player player && !player.level().isClientSide()) {
            WPCompat.removeEffect(player, WPCompat.EFFECT_PARALYSIS);
        }
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
        tooltip.add(Component.translatable("item.whispering.pioneer_core.tooltip.flight"));
        tooltip.add(Component.translatable("item.whispering.pioneer_core.tooltip.damage"));
        tooltip.add(Component.translatable("item.whispering.pioneer_core.tooltip.paralysis"));
    }

    public static boolean isWorn(LivingEntity entity) {
        return AccessoryHelper.isWorn(entity, AllCurios.PIONEER_CORE);
    }

    public static boolean hasFuel(Player player) {
        Inventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            if (inventory.getItem(i).is(Items.STICK)) {
                return true;
            }
        }
        return false;
    }

    public static boolean consumeFuel(Player player) {
        Inventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack slot = inventory.getItem(i);
            if (slot.is(Items.STICK)) {
                slot.shrink(1);
                return true;
            }
        }
        return false;
    }
}
