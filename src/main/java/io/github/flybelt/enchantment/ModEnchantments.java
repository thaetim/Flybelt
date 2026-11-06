package io.github.flybelt.enchantment;

import io.github.flybelt.FlybeltMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

public final class ModEnchantments {
    public static final ResourceKey<Enchantment> LEVITATION = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(FlybeltMod.MODID, "levitation"));

    private ModEnchantments() {
    }

    public static int getLevel(Level level, ItemStack stack) {
        Registry<Enchantment> enchantments = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT);
        return enchantments.getHolder(LEVITATION)
                .map(stack::getEnchantmentLevel)
                .orElse(0);
    }
}
