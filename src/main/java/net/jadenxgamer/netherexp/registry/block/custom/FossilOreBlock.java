package net.jadenxgamer.netherexp.registry.block.custom;

import net.jadenxgamer.netherexp.config.JNEConfigs;
import net.jadenxgamer.netherexp.registry.block.JNEBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class FossilOreBlock extends Block {

    public FossilOreBlock(Properties properties) {
        super(properties);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        float conversionOdds = random.nextInt(50);
        boolean sideChecks = level.getBlockState(pos.above()).isSolid() &&
                level.getBlockState(pos.below()).isSolid() &&
                level.getBlockState(pos.north()).isSolid() &&
                level.getBlockState(pos.south()).isSolid() &&
                level.getBlockState(pos.east()).isSolid() &&
                level.getBlockState(pos.west()).isSolid();
        if (conversionOdds == 0 && sideChecks && JNEConfigs.RENEWABLE_FOSSIL_FUEL.get()) {
            level.setBlock(pos, JNEBlocks.FOSSIL_FUEL_ORE.get().defaultBlockState(), UPDATE_CLIENTS);
            level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0f, 1.0f);
        }
    }
}
