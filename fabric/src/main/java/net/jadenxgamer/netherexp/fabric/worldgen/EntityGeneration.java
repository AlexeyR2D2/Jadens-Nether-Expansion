package net.jadenxgamer.netherexp.fabric.worldgen;

import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.jadenxgamer.netherexp.NetherExp;
import net.jadenxgamer.netherexp.compat.CompatUtil;
import net.jadenxgamer.netherexp.registry.entity.JNEEntityType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.Heightmap;

public class EntityGeneration {
    public static void generateEntities() {
    BiomeModifications.create(new ResourceLocation(NetherExp.MOD_ID, "soul_sand_valley_spawn")).add(ModificationPhase.ADDITIONS, BiomeSelectors.includeByKey(Biomes.SOUL_SAND_VALLEY), (selectionContext, modificationContext) -> {
            BiomeModificationContext.SpawnSettingsContext settings = modificationContext.getSpawnSettings();
            settings.setSpawnCost(JNEEntityType.VESSEL.get(), 0.7, 0.15);
            settings.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(JNEEntityType.VESSEL.get(), 30, 1, 1));
            settings.setSpawnCost(JNEEntityType.APPARITION.get(), 0.7, 0.15);
            settings.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(JNEEntityType.APPARITION.get(), 20, 1, 1));
        });
        if (CompatUtil.compatGardensOfTheDead()) {
            BiomeModifications.create(new ResourceLocation(NetherExp.MOD_ID, "soulblight_forest_spawn")).add(ModificationPhase.ADDITIONS, BiomeSelectors.includeByKey(CompatUtil.BiomeKeys.SOULBLIGHT_FOREST), (selectionContext, modificationContext) -> {
                BiomeModificationContext.SpawnSettingsContext settings = modificationContext.getSpawnSettings();
                settings.setSpawnCost(JNEEntityType.VESSEL.get(), 1.0, 0.8);
                settings.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(JNEEntityType.VESSEL.get(), 30, 1, 1));
                settings.setSpawnCost(JNEEntityType.APPARITION.get(), 1.0, 0.8);
                settings.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(JNEEntityType.APPARITION.get(), 20, 1, 1));
            });
        }

        SpawnPlacements.register(JNEEntityType.VESSEL.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        SpawnPlacements.register(JNEEntityType.APPARITION.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        SpawnPlacements.register(JNEEntityType.BANSHEE.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
    }
}
