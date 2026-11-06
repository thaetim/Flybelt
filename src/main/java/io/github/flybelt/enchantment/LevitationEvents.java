package io.github.flybelt.enchantment;

import io.github.flybelt.FlybeltMod;
import io.github.flybelt.item.ModItems;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;

@EventBusSubscriber(modid = FlybeltMod.MODID)
public final class LevitationEvents {
    private LevitationEvents() {
    }

    @SubscribeEvent
    public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
        if (event.getSlot().getType() != EquipmentSlot.Type.HUMANOID_ARMOR || !(event.getEntity() instanceof Player player)) {
            return;
        }

        int levitationLevel = ModEnchantments.getLevel(player.level(), player.getItemBySlot(event.getSlot()));
        if (levitationLevel > 0
                && !player.hasEffect(MobEffects.LEVITATION)
                && !player.getItemBySlot(EquipmentSlot.LEGS).is(ModItems.FLYBELT.get())) {
            player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 100 * levitationLevel, levitationLevel - 1));
        }
    }
}
