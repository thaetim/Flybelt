package io.github.flybelt.item;
import io.github.flybelt.FlybeltMod;
import io.github.flybelt.item.custom.ModArmorItem;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = 
        DeferredRegister.create(ForgeRegistries.ITEMS, FlybeltMod.MODID);

    public static final RegistryObject<Item> RAW_REDSTONE_ALLOY = ITEMS.register("raw_redstone_alloy",
        () -> new Item(new Properties()));
    public static final RegistryObject<Item> REDSTONE_ALLOY_INGOT = ITEMS.register("redstone_alloy_ingot",
        () -> new Item(new Properties()));

    public static final RegistryObject<Item> FLYBELT = ITEMS.register("flybelt",
        () -> new ModArmorItem(ModArmorMaterials.REDSTONE_ALLOY, ArmorItem.Type.CHESTPLATE, new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
