package io.github.flybelt.effect;

import io.github.flybelt.item.ModItems;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

public class FlightEffect extends MobEffect {

    protected FlightEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    private int sumArmorProtection(Player player) {
        int armorWeight = 0;

        for (ItemStack armorSlot : player.getArmorSlots()) {
            if (armorSlot.getItem() instanceof ArmorItem) {
                ArmorItem armorPiece = (ArmorItem) armorSlot.getItem();
                armorWeight = armorWeight + armorPiece.getDefense();
            }
        }

        return armorWeight;
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.level().isClientSide()) {
            if (pLivingEntity instanceof Player) {
                Player player = (Player) pLivingEntity;
                if (!player.isCreative() && !player.isSpectator()) {
                    // flying ability logic
                    if (player.fallDistance > 3.0) {
                        player.getAbilities().mayfly = false;
                    } else {
                        player.getAbilities().mayfly = true;
                        /*
                         * assuming creative flying speed is 2.5x walking speed
                         * here flying speed should be = walking speed for armor defense = 1
                         * 
                         * hyperbolic function scaled so that if
                         * sum of protection = 0, then fly speed = 0.02F (default flying speed)
                         * sum of protection = 1 (default for only flybelt worn), fly speed = 0.008F
                         * (default walking speed ~ 0.4 * creative flying speed)
                         * amplifier 1 ~ x1
                         * amplifier 2 ~ x1.5
                         * amplifier 3 ~ x2
                         */
                        player.getAbilities().setFlyingSpeed((1.0F + ((float) pAmplifier / 2.0F))
                                / (float) (75 * sumArmorProtection(player) + 50));
                    }
                    // hunger penalty logic
                    if (player.getAbilities().flying) {
                        // starting from 4x standard hunger and going up normally from there
                        player.causeFoodExhaustion(0.005F * (float) (pAmplifier + 5));
                    }
                    // update ability changes
                    player.onUpdateAbilities();
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
                if (!player.isCreative() && !player.isSpectator()) {
                    player.getAbilities().mayfly = false;
                    if (!ItemStack.isSameItem(player.getInventory().armor.get(2),
                            new ItemStack(ModItems.FLYBELT.get()))) {
                        player.getAbilities().flying = false;
                        player.getAbilities().setFlyingSpeed(0.05f);
                    }
                    player.onUpdateAbilities();
                }
            }
        }
        super.removeAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
    }
}
