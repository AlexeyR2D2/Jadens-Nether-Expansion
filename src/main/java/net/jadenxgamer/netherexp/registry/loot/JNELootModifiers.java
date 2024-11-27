package net.jadenxgamer.netherexp.registry.loot;

import com.mojang.serialization.Codec;
import net.jadenxgamer.netherexp.NetherExp;
import net.jadenxgamer.netherexp.registry.loot.custom.AddEnchantedItemModifier;
import net.jadenxgamer.netherexp.registry.loot.custom.AddItemModifier;
import net.jadenxgamer.netherexp.registry.loot.custom.ReplaceItemModifier;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class JNELootModifiers {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, NetherExp.MOD_ID);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADD_ITEM = LOOT_MODIFIERS.register("add_item", AddItemModifier.CODEC);
    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> REPLACE_ITEM = LOOT_MODIFIERS.register("replace_item", ReplaceItemModifier.CODEC);
    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADD_ENCHANTED_ITEM = LOOT_MODIFIERS.register("add_enchanted_item", AddEnchantedItemModifier.CODEC);

    public static void init(IEventBus eventBus) {
        LOOT_MODIFIERS.register(eventBus);
    }
}
