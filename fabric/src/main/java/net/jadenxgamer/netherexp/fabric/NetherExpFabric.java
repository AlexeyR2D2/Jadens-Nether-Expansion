package net.jadenxgamer.netherexp.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.jadenxgamer.netherexp.NetherExp;
import net.jadenxgamer.netherexp.fabric.worldgen.EntityGeneration;
import net.jadenxgamer.netherexp.fabric.worldgen.SoulSandValleyFeatures;
import net.jadenxgamer.netherexp.registry.entity.JNEEntityType;
import net.jadenxgamer.netherexp.registry.entity.custom.Apparition;
import net.jadenxgamer.netherexp.registry.entity.custom.EctoSlab;
import net.jadenxgamer.netherexp.registry.entity.custom.Vessel;
import net.jadenxgamer.netherexp.registry.entity.custom.Wisp;
import net.jadenxgamer.netherexp.registry.item.brewing.JNEPotionRecipe;

public class NetherExpFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        NetherExp.init();
        SoulSandValleyFeatures.generateFeatures();
        EntityGeneration.generateEntities();
        JNEPotionRecipe.addInvokerPotionRecipes();

        FabricDefaultAttributeRegistry.register(JNEEntityType.APPARITION.get(), Apparition.createAttributes());
        FabricDefaultAttributeRegistry.register(JNEEntityType.WISP.get(), Wisp.createAttributes());
        FabricDefaultAttributeRegistry.register(JNEEntityType.VESSEL.get(), Vessel.createAttributes());
        FabricDefaultAttributeRegistry.register(JNEEntityType.ECTO_SLAB.get(), EctoSlab.createAttributes());
    }
}
