package io.github.flybelt.item.custom;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import io.github.flybelt.item.ModArmorMaterials;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ModArmorItem extends ArmorItem{
    private static final Map<ArmorMaterial, MobEffectInstance> MATERIAL_TO_EFFECT_MAP =
        (new ImmutableMap.Builder<ArmorMaterial, MobEffectInstance>())
            .put(ModArmorMaterials.REDSTONE_ALLOY, new MobEffectInstance(MobEffects.HUNGER, 200, 10,
                false, false, true)).build();

    public ModArmorItem(ArmorMaterial pArmorMaterial, Type pType, Properties pProperties) {
        super(pArmorMaterial, pType, pProperties);
    }

    @Override
    public void onArmorTick(ItemStack stack, Level world, Player player) {
        if(!world.isClientSide()) { // are we on the server?
            evaluateArmorEffects(player);
        }
    }

    private void evaluateArmorEffects(Player player) {
        for (Map.Entry<ArmorMaterial, MobEffectInstance> entry : MATERIAL_TO_EFFECT_MAP.entrySet()) {
            ArmorMaterial mapArmorMaterial = entry.getKey();
            MobEffectInstance mapStatusEffect = entry.getValue();

            if(hasCorrectArmorOn(mapArmorMaterial, player)) {
                addStatusEffectForMaterial(player, mapArmorMaterial, mapStatusEffect);
            }
        }
    }

    private void addStatusEffectForMaterial(Player player, ArmorMaterial mapArmorMaterial, MobEffectInstance mapStatusEffect) {
        boolean hasPlayerEffect = player.hasEffect(mapStatusEffect.getEffect());

        if(hasCorrectArmorOn(mapArmorMaterial, player) && !hasPlayerEffect) {
            player.addEffect(new MobEffectInstance(mapStatusEffect));
        }
    }

    private boolean hasCorrectArmorOn(ArmorMaterial material, Player player) {
        Item breastplate = player.getInventory().getArmor(2).getItem();
        if (!(breastplate instanceof ArmorItem)) {
            return false;
        }
        return  ((ArmorItem)breastplate).getMaterial() == material;
    }
}
