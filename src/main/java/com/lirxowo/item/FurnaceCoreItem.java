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
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FurnaceCoreItem extends AccessoryItem {

    private static final ResourceLocation DAMAGE_MODIFIER_ID = new ResourceLocation(Whispering.MOD_ID, "furnace_core");
    private static final double DAMAGE_BONUS = 0.10D;

    private static final int INTERVAL_TICKS = 20;
    private static final int FUEL_CONSUME_INTERVAL_TICKS = 240;
    private static final int BUFF_DURATION_TICKS = 60;
    private static final int FULL_FOOD_LEVEL = 20;
    private static final float REFILL_SATURATION = 6.0F;

    public FurnaceCoreItem(Properties properties) {
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
    public void tick(ItemStack stack, SlotReference reference) {
        if (!(reference.entity() instanceof Player player) || player.level().isClientSide()) {
            return;
        }
        if (player.tickCount % INTERVAL_TICKS != 0) {
            return;
        }
        Item fuel = WPCompat.item(WPCompat.ITEM_LAVA_POWER_CELL);
        if (fuel == null || !hasItem(player, fuel)) {
            return;
        }
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, BUFF_DURATION_TICKS, 1, false, true));
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, BUFF_DURATION_TICKS, 1, false, true));

        if (player.tickCount % FUEL_CONSUME_INTERVAL_TICKS != 0) {
            return;
        }
        FoodData foodData = player.getFoodData();
        if (foodData.getFoodLevel() < FULL_FOOD_LEVEL && consumeItem(player, fuel)) {
            foodData.setFoodLevel(FULL_FOOD_LEVEL);
            foodData.setSaturation(REFILL_SATURATION);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.whispering.furnace_core.tooltip.fuel"));
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

    private static boolean consumeItem(Player player, Item item) {
        Inventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack slot = inventory.getItem(i);
            if (slot.is(item)) {
                slot.shrink(1);
                return true;
            }
        }
        return false;
    }
}
