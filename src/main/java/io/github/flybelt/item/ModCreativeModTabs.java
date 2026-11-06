package io.github.flybelt.item;

import io.github.flybelt.FlybeltMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister
            .create(Registries.CREATIVE_MODE_TAB, FlybeltMod.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> FLYBELT_TAB = CREATIVE_MODE_TABS.register("flybelt",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.EMERALD_FEATHER.get()))
                    .title(Component.translatable("creativetab.flybelt_tab"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.EMERALD_FEATHER.get());
                        output.accept(ModItems.FLYBELT.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
