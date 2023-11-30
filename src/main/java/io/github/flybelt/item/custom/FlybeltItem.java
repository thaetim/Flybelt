package io.github.flybelt.item.custom;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import io.github.flybelt.effect.ModEffects;
import io.github.flybelt.item.ModArmorMaterials;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FlybeltItem extends ArmorItem {
    private static final Map<ArmorMaterial, MobEffectInstance[]> MATERIAL_TO_EFFECT_MAP = ImmutableMap
            .<ArmorMaterial, MobEffectInstance[]>builder()
            .put(ModArmorMaterials.REDSTONE_ALLOY, new MobEffectInstance[] {
                    new MobEffectInstance(MobEffects.HUNGER, 200, 5, false, false, false),
                    new MobEffectInstance(ModEffects.FLIGHT.get(), 200, 2, false, false, false)
            })
            .build();

    public FlybeltItem(ArmorMaterial pArmorMaterial, Type pType, Properties pProperties) {
        super(pArmorMaterial, pType, pProperties);
    }

    @Override
    public void onArmorTick(ItemStack stack, Level world, Player player) {
        if (!world.isClientSide()) { // are we on the server?
            evaluateArmorEffects(player);
        }
    }

    private void evaluateArmorEffects(Player player) {
        for (Map.Entry<ArmorMaterial, MobEffectInstance[]> entry : MATERIAL_TO_EFFECT_MAP.entrySet()) {
            ArmorMaterial mapArmorMaterial = entry.getKey();
            MobEffectInstance[] mapStatusEffects = entry.getValue();

            if (mapStatusEffects != null) {
                for (MobEffectInstance mapStatusEffect : mapStatusEffects) {
                    addStatusEffectForMaterial(player, mapArmorMaterial, mapStatusEffect);
                }
            }
        }
    }

    private void addStatusEffectForMaterial(Player player, ArmorMaterial mapArmorMaterial,
            MobEffectInstance mapStatusEffect) {
        boolean hasPlayerEffect = player.hasEffect(mapStatusEffect.getEffect());

        if (!hasPlayerEffect) {
            player.addEffect(new MobEffectInstance(mapStatusEffect));
        }
    }
}
