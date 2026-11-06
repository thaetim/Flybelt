package io.github.flybelt.effect;

import io.github.flybelt.FlybeltMod;
import io.github.flybelt.enchantment.ModEnchantments;
import io.github.flybelt.item.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class FlightEffect extends MobEffect {
    private static final int GRACE_TICKS = 100;
    private static final ResourceLocation FLIGHT_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath(FlybeltMod.MODID, "flight");
    private static final AttributeModifier FLIGHT_MODIFIER = new AttributeModifier(FLIGHT_MODIFIER_ID, 1.0,
            AttributeModifier.Operation.ADD_VALUE);
    private static final Set<UUID> REPLACING = new HashSet<>();

    private static void setCreativeFlight(Player player, boolean allowed) {
        AttributeInstance flight = player.getAttribute(NeoForgeMod.CREATIVE_FLIGHT);
        if (flight == null) {
            return;
        }
        if (allowed) {
            flight.addOrUpdateTransientModifier(FLIGHT_MODIFIER);
        } else {
            flight.removeModifier(FLIGHT_MODIFIER_ID);
        }
    }

    public FlightEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    private int sumArmorDefense(Player player) {
        int armorDefenseSum = 0;
        for (ItemStack armorSlot : player.getArmorSlots()) {
            if (armorSlot.getItem() instanceof ArmorItem armorPiece) {
                armorDefenseSum += armorPiece.getDefense();
            }
        }
        return armorDefenseSum;
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.level().isClientSide() && livingEntity instanceof Player player
                && !player.isCreative() && !player.isSpectator()) {
            if (player.fallDistance > 3.0F) {
                setCreativeFlight(player, false);
            } else {
                setCreativeFlight(player, true);
                /*
                 * scaling flight speed depending on currently worn armor
                 *
                 * hyperbolic function scaled so that if
                 * armor defense = 1 (only flybelt) and amplifier = 3
                 * then speed = 0.05f (default for creative mode)
                 */
                player.getAbilities().setFlyingSpeed((float) (1 + (amplifier / 2))
                        / (float) (20 * (1.5 * sumArmorDefense(player) + 1)));
            }
            if (player.getAbilities().flying && player.getFoodData().getFoodLevel() > 0) {
                player.causeFoodExhaustion(0.005F * (amplifier + 5));
            }
            player.onUpdateAbilities();
        }
        return super.applyEffectTick(livingEntity, amplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    private static void setFlightEffect(Player player, int duration, int amplifier) {
        UUID playerId = player.getUUID();
        REPLACING.add(playerId);
        try {
            player.removeEffect(ModMobEffects.FLIGHT);
            player.addEffect(new MobEffectInstance(ModMobEffects.FLIGHT, duration, amplifier, false, false, true));
        } finally {
            REPLACING.remove(playerId);
        }
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
            if (player.isCreative() || player.isSpectator()) {
                if (player.hasEffect(ModMobEffects.FLIGHT)) {
                    player.removeEffect(ModMobEffects.FLIGHT);
                }
                return;
            }

            ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
            int levitationLevel = legs.is(ModItems.FLYBELT.get()) ? ModEnchantments.getLevel(player.level(), legs) : 0;
            boolean canFly = levitationLevel > 0 && player.getFoodData().getFoodLevel() > 0;
            MobEffectInstance flight = player.getEffect(ModMobEffects.FLIGHT);

            if (canFly) {
                int amplifier = levitationLevel - 1;
                if (flight == null || !flight.isInfiniteDuration() || flight.getAmplifier() != amplifier) {
                    setFlightEffect(player, MobEffectInstance.INFINITE_DURATION, amplifier);
                }
            } else if (flight != null && flight.isInfiniteDuration()) {
                setFlightEffect(player, GRACE_TICKS, flight.getAmplifier());
            }
        }

        @SubscribeEvent
        public static void onExpired(MobEffectEvent.Expired event) {
            endFlight(event.getEntity(), event.getEffectInstance());
        }

        @SubscribeEvent
        public static void onRemoved(MobEffectEvent.Remove event) {
            endFlight(event.getEntity(), event.getEffectInstance());
        }

        private static void endFlight(LivingEntity entity, @Nullable MobEffectInstance instance) {
            if (instance == null || !instance.getEffect().is(ModMobEffects.FLIGHT.getKey())) {
                return;
            }
            if (!(entity instanceof Player player) || player.level().isClientSide() || REPLACING.contains(player.getUUID())) {
                return;
            }

            player.getAbilities().setFlyingSpeed(0.05F);
            setCreativeFlight(player, false);
            if (!player.isCreative() && !player.isSpectator()) {
                player.getAbilities().flying = false;
            }
            player.onUpdateAbilities();
        }
    }
}
