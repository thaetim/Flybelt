package io.github.flybelt.effect;

import io.github.flybelt.enchantment.ModEnchantments;
import io.github.flybelt.item.ModItems;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

public class FlightEffect extends MobEffect {

    private float defaultFlightSpeed = 0.05f;

    protected FlightEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    private int sumArmorDefense(Player player) {
        int armorDefenseSum = 0;

        for (ItemStack armorSlot : player.getArmorSlots()) {
            if (armorSlot.getItem() instanceof ArmorItem) {
                ArmorItem armorPiece = (ArmorItem) armorSlot.getItem();
                armorDefenseSum = armorDefenseSum + armorPiece.getDefense();
            }
        }

        return armorDefenseSum;
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.level().isClientSide()) {
            if (pLivingEntity instanceof Player) {
                Player player = (Player) pLivingEntity;
                // System.out.println(player.getAbilities().getFlyingSpeed());
                if (!player.isCreative() && !player.isSpectator() && (player.getInventory().armor.get(2)
                        .getEnchantmentLevel(ModEnchantments.LEVITATION.get()) > 0)) {

                    // flying ability logic
                    if (player.fallDistance > 3.0) {
                        player.getAbilities().mayfly = false;
                    } else {
                        player.getAbilities().mayfly = true;
                        /*
                         * scaling flight speed depending on currently worn armor
                         * 
                         * hyperbolic function scaled so that if
                         * armor defense = 1 (only flybelt) and amplifier = 3
                         * then speed = 0.05f (default for creative mode)
                         */
                        player.getAbilities().setFlyingSpeed((float) (1 + (pAmplifier / 2))
                                / (float) (20 * (1.5 * sumArmorDefense(player) + 1)));
                    }
                    // hunger penalty logic
                    if (player.getAbilities().flying) {
                        // starting from 4x standard hunger and scaling up with amplifier normally from
                        // there
                        player.causeFoodExhaustion(0.005F * (float) (pAmplifier + 5));
                    }
                    // update ability changes
                    player.onUpdateAbilities();
                } else {
                    player.getAbilities().setFlyingSpeed(defaultFlightSpeed);
                }
            }
        }
        super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

    @Override
    public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        if (!pLivingEntity.level().isClientSide()) {
            if (pLivingEntity instanceof Player) {
                Player player = (Player) pLivingEntity;
                if (!player.getInventory().armor.get(2).getItem().equals(ModItems.FLYBELT.get())) {
                    player.getAbilities().setFlyingSpeed(defaultFlightSpeed);
                    if (!player.isCreative() && !player.isSpectator()) {
                        player.getAbilities().mayfly = false;
                        player.getAbilities().flying = false;
                    }
                }
                player.onUpdateAbilities();
            }
        }
        super.removeAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
    }
}
