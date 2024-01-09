package io.github.flybelt.enchantment;

import io.github.flybelt.item.ModItems;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LevitationEnchantment extends Enchantment {
    public LevitationEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @SubscribeEvent
    public void onEquipmentChange(LivingEquipmentChangeEvent event) {
        if (event.getSlot().isArmor() && event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            int levitationEnchantmentLevel = EnchantmentHelper.getTagEnchantmentLevel(this,
                    player.getItemBySlot(event.getSlot()));

            // add levitation effect unless already added or wearing the flybelt
            if (!player.hasEffect(MobEffects.LEVITATION) && (levitationEnchantmentLevel > 0)
                    && (player.getInventory().armor.get(2).getItem() != ModItems.FLYBELT.get())) {
                player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 100 * levitationEnchantmentLevel,
                        levitationEnchantmentLevel - 1));
            }
        }
    }
}
