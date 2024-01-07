package io.github.flybelt.item.custom;

import io.github.flybelt.effect.ModMobEffects;
import io.github.flybelt.enchantment.ModEnchantments;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FlybeltItem extends ArmorItem {
    public FlybeltItem(ArmorMaterial pArmorMaterial, Type pType, Properties pProperties) {
        super(pArmorMaterial, pType, pProperties);
    }

    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        if (!level.isClientSide()) { // are we on the server?
            if (!player.hasEffect(ModMobEffects.FLIGHT.get())) {
                int levitationEnchantmentLevel = stack.getEnchantmentLevel(ModEnchantments.LEVITATION.get());
                if (levitationEnchantmentLevel > 0) {
                    player.addEffect(
                            new MobEffectInstance(ModMobEffects.FLIGHT.get(), 100, levitationEnchantmentLevel - 1,
                                    false, false, false));
                }
            }
        }
    }
}
