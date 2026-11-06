package io.github.flybelt.item.custom;

import io.github.flybelt.FlybeltMod;
import io.github.flybelt.effect.ModMobEffects;
import io.github.flybelt.enchantment.ModEnchantments;
import io.github.flybelt.item.ModItems;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.core.Holder;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class FlybeltItem extends ArmorItem {
    public FlybeltItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @EventBusSubscriber(modid = FlybeltMod.MODID)
    public static final class Events {
        private Events() {
        }

        @SubscribeEvent
        public static void onPlayerTick(PlayerTickEvent.Post event) {
            Player player = event.getEntity();
            if (player.level().isClientSide()) {
                return;
            }

            var legs = player.getItemBySlot(EquipmentSlot.LEGS);
            if (!legs.is(ModItems.FLYBELT.get()) || player.hasEffect(ModMobEffects.FLIGHT)) {
                return;
            }

            int levitationLevel = ModEnchantments.getLevel(player.level(), legs);
            if (levitationLevel > 0) {
                player.addEffect(new MobEffectInstance(ModMobEffects.FLIGHT, 100, levitationLevel - 1, false, false, false));
            }
        }
    }
}
