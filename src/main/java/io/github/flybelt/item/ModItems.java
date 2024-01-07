package io.github.flybelt.item;

import io.github.flybelt.FlybeltMod;
import io.github.flybelt.item.custom.FlybeltItem;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
        public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
                        FlybeltMod.MODID);

        public static final RegistryObject<Item> EMERALD_FEATHER = ITEMS.register("emerald_feather",
                        () -> new Item(new Properties()));

        public static final RegistryObject<Item> FLYBELT = ITEMS.register("flybelt",
                        () -> new FlybeltItem(ModArmorMaterials.FLYBELT_MATERIAL, ArmorItem.Type.CHESTPLATE,
                                        new Item.Properties()));

        public static void register(IEventBus eventBus) {
                ITEMS.register(eventBus);
        }
}
