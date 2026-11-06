package io.github.flybelt.item;

import io.github.flybelt.FlybeltMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ModArmorMaterials {
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(Registries.ARMOR_MATERIAL,
            FlybeltMod.MODID);

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> FLYBELT_MATERIAL = ARMOR_MATERIALS.register("flybelt_material",
            () -> new ArmorMaterial(defense(), 25, SoundEvents.ARMOR_EQUIP_ELYTRA,
                    () -> Ingredient.of(Items.REDSTONE_BLOCK),
                    List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(FlybeltMod.MODID, "flybelt_material"))),
                    0.0F, 0.0F));

    private static Map<ArmorItem.Type, Integer> defense() {
        EnumMap<ArmorItem.Type, Integer> defense = new EnumMap<>(ArmorItem.Type.class);
        for (ArmorItem.Type type : ArmorItem.Type.values()) {
            defense.put(type, 1);
        }
        return defense;
    }

    public static void register(IEventBus eventBus) {
        ARMOR_MATERIALS.register(eventBus);
    }
}
