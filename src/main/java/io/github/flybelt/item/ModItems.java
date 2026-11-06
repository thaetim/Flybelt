package io.github.flybelt.item;

import io.github.flybelt.FlybeltMod;
import io.github.flybelt.item.custom.FlybeltItem;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(FlybeltMod.MODID);

    public static final DeferredItem<Item> EMERALD_FEATHER = ITEMS.registerItem("emerald_feather", Item::new);

    // Leggings used a custom durability base of 7 with a multiplier of 25.
    public static final DeferredItem<FlybeltItem> FLYBELT = ITEMS.register("flybelt",
            () -> new FlybeltItem(ModArmorMaterials.FLYBELT_MATERIAL, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(175)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
