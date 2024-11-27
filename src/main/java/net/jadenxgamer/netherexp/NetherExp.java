package net.jadenxgamer.netherexp;

import com.mojang.logging.LogUtils;
import net.jadenxgamer.netherexp.compat.CompatUtil;
import net.jadenxgamer.netherexp.config.JNEConfigs;
import net.jadenxgamer.netherexp.config.JNEForgeConfigs;
import net.jadenxgamer.netherexp.registry.advancements.JNECriteriaTriggers;
import net.jadenxgamer.netherexp.registry.block.JNEBlockEntityType;
import net.jadenxgamer.netherexp.registry.block.JNEBlocks;
import net.jadenxgamer.netherexp.registry.effect.JNEMobEffects;
import net.jadenxgamer.netherexp.registry.enchantment.JNEEnchantments;
import net.jadenxgamer.netherexp.registry.entity.JNEEntityType;
import net.jadenxgamer.netherexp.registry.entity.custom.*;
import net.jadenxgamer.netherexp.registry.fluid.JNEFluids;
import net.jadenxgamer.netherexp.registry.item.JNECreativeModeTabs;
import net.jadenxgamer.netherexp.registry.item.JNEItems;
import net.jadenxgamer.netherexp.registry.item.brewing.JNEPotionRecipe;
import net.jadenxgamer.netherexp.registry.loot.JNELootModifiers;
import net.jadenxgamer.netherexp.registry.misc_registry.JNEBuiltinPacks;
import net.jadenxgamer.netherexp.registry.misc_registry.JNEPaintings;
import net.jadenxgamer.netherexp.registry.misc_registry.JNESoundEvents;
import net.jadenxgamer.netherexp.registry.particle.JNEParticleTypes;
import net.jadenxgamer.netherexp.registry.worldgen.JNEBiomeModifiers;
import net.jadenxgamer.netherexp.registry.worldgen.feature.JNEFeature;
import net.jadenxgamer.netherexp.registry.worldgen.structure.JNEStructureType;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(NetherExp.MOD_ID)
public class NetherExp {
    public static final String MOD_ID = "netherexp";
    private static final Logger LOGGER = LogUtils.getLogger();

    public NetherExp() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> NetherExpClient::init);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, JNEForgeConfigs.COMMON);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(NetherExp::registerAttributes);
        modEventBus.addListener(NetherExp::registerSpawnPlacements);
        modEventBus.addListener(NetherExp::loadComplete);
        modEventBus.addListener(NetherExp::addBuiltinPacks);
        MinecraftForge.EVENT_BUS.register(this);

        JNECriteriaTriggers.init();
        JNECreativeModeTabs.init(modEventBus);
        JNESoundEvents.init(modEventBus);
        JNEParticleTypes.init(modEventBus);
        JNEEnchantments.init(modEventBus);
        JNEMobEffects.init(modEventBus);

        JNEEntityType.init(modEventBus);
        JNEStructureType.init(modEventBus);
        JNEFluids.init(modEventBus);
        JNEPaintings.init(modEventBus);
        JNEBlocks.init(modEventBus);
        JNEFeature.init(modEventBus);
        JNEBlockEntityType.init(modEventBus);
        JNEItems.init(modEventBus);

        JNEBiomeModifiers.init(modEventBus);
        JNELootModifiers.init(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(JNEPotionRecipe::addInvokerPotionRecipes);
    }

    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(JNEEntityType.APPARITION.get(), Apparition.createAttributes().build());
        event.put(JNEEntityType.WISP.get(), Wisp.createAttributes().build());
        event.put(JNEEntityType.VESSEL.get(), Vessel.createAttributes().build());
        event.put(JNEEntityType.ECTO_SLAB.get(), EctoSlab.createAttributes().build());
        event.put(JNEEntityType.BANSHEE.get(), Banshee.createAttributes().build());
        event.put(JNEEntityType.STAMPEDE.get(), Stampede.createAttributes().build());
        event.put(JNEEntityType.CARCASS.get(), Carcass.createAttributes().build());
    }

    public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
        event.register(JNEEntityType.VESSEL.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules,
                SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(JNEEntityType.APPARITION.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules,
                SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(JNEEntityType.BANSHEE.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules,
                SpawnPlacementRegisterEvent.Operation.REPLACE);
    }

    public static void loadComplete(FMLLoadCompleteEvent event) {
        event.enqueueWork(JNEFluids::initFluidInteractions);
    }

    private static void addBuiltinPacks(AddPackFindersEvent event) {
        // ResourcePacks
        if (event.getPackType() == PackType.CLIENT_RESOURCES) {
            JNEBuiltinPacks.rpJNERetextures(event);
            JNEBuiltinPacks.rpConflictingRetextures(event);
            JNEBuiltinPacks.rpUniqueNetherWood(event);
        }
        // Datapacks
        if (event.getPackType() == PackType.SERVER_DATA) {
            if (JNEConfigs.LARGER_NETHER_BIOMES.get()) {
                JNEBuiltinPacks.dpLargerNetherBiomes(event);
            }
            if (CompatUtil.checkNethersDelight()) {
                JNEBuiltinPacks.dpNethersDelightCompat(event);
            }
            if (CompatUtil.checkAlexsCaves()) {
                JNEBuiltinPacks.dpAlexCavesCompat(event);
            }
            if (CompatUtil.checkGardensOfTheDead()) {
                JNEBuiltinPacks.dpGardensOfTheDeadCompat(event);
            }
            if (CompatUtil.checkCavernsAndChasms()) {
                JNEBuiltinPacks.dpCavernsAndChasmsCompat(event);
            }
        }
    }
}
