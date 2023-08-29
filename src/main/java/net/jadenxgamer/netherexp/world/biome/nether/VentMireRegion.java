package net.jadenxgamer.netherexp.world.biome.nether;

import com.mojang.datafixers.util.Pair;
import net.jadenxgamer.netherexp.world.biome.ModBiomes;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import terrablender.api.Region;
import terrablender.api.RegionType;

import java.util.function.Consumer;

public class VentMireRegion extends Region {
    public VentMireRegion(Identifier name, int weight) {
        super(name, RegionType.NETHER, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> mapper) {
        this.addBiome(mapper,
                //Temperature
                MultiNoiseUtil.ParameterRange.of(-0.4F),
                //Humidity
                MultiNoiseUtil.ParameterRange.of(0.3F),
                //Continentalness
                MultiNoiseUtil.ParameterRange.of(0.0F),
                //Erosion
                MultiNoiseUtil.ParameterRange.of(0.0F),
                //Weirdness
                MultiNoiseUtil.ParameterRange.of(0.0F),
                //Depth
                MultiNoiseUtil.ParameterRange.of(0.0F),
                0.0F, ModBiomes.VENT_MIRE);
    }
}
