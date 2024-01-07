package io.github.flybelt.item;

import io.github.flybelt.FlybeltMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister
            .create(Registries.CREATIVE_MODE_TAB, FlybeltMod.MODID);

    public static final RegistryObject<CreativeModeTab> FLYBELT_TAB = CREATIVE_MODE_TABS.register("flybelt",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.EMERALD_FEATHER.get()))
                    .title(Component.translatable("creativetab.flybelt_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        // the order of items here is the order in the tab
                        pOutput.accept(ModItems.EMERALD_FEATHER.get());
                        pOutput.accept(ModItems.FLYBELT.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }

}
