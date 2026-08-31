package me.jwaxy.mobchains.item;

import me.jwaxy.mobchains.MobChains;
import me.jwaxy.mobchains.item.impl.MobChainItem;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.function.Function;

public class ModItems {
    public static final Item MOB_CHAIN = registerItem("mob_chain", MobChainItem::new);

    private static Item registerItem(String name, Function<Item.Properties, Item> function) {
        Identifier id = MobChains.id(name);
        return Registry.register(BuiltInRegistries.ITEM, id,
                function.apply(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    }

    public static void registerModItems() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(output -> {
            output.insertAfter(Items.LEAD, MOB_CHAIN);
        });
    }
}