package io.github.flybelt.effect;

import io.github.flybelt.item.ModItems;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class FlightEffect extends MobEffect {

    protected FlightEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.level().isClientSide()) {
            if (pLivingEntity instanceof Player) {
                Player player = (Player) pLivingEntity;
                if (!player.isCreative() && !player.isSpectator()) {
                    if (player.fallDistance > 3.0) {
                        player.getAbilities().mayfly = false;
                    } else {
                        player.getAbilities().mayfly = true;
                        player.getAbilities().setFlyingSpeed(0.01f);
                    }
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
                    player.getAbilities().setFlyingSpeed(0.05f);
                    if (!ItemStack.isSameItem(player.getInventory().armor.get(2),
                            new ItemStack(ModItems.FLYBELT.get()))) {
                        player.getAbilities().flying = false;
                    }
                    player.onUpdateAbilities();
                }
            }
        }
        super.removeAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
    }
}
