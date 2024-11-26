package net.jadenxgamer.netherexp.registry.block.custom;

import com.google.common.collect.Maps;
import dev.architectury.core.block.ArchitecturyLiquidBlock;
import net.jadenxgamer.netherexp.config.JNEConfigs;
import net.jadenxgamer.netherexp.registry.block.JNEBlocks;
import net.jadenxgamer.netherexp.registry.misc_registry.JNESoundEvents;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FlowingFluid;

import java.util.Map;
import java.util.function.Supplier;

public class EctoplasmLiquidBlock extends ArchitecturyLiquidBlock {
    /*
     * Based on Alex's Caves AcidBlock.java code with a few tweaks
     * https://github.com/AlexModGuy/AlexsCaves/blob/main/src/main/java/com/github/alexmodguy/alexscaves/server/block/AcidBlock.java
     * https://www.curseforge.com/minecraft/mc-mods/alexs-caves
     */

    private static Map<Block, Block> FREEZES;

    public EctoplasmLiquidBlock(Supplier<? extends FlowingFluid> fluid, Properties properties) {
        super(fluid, properties);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean bl) {
        super.onPlace(state, level, pos, oldState, bl);
        if (JNEConfigs.ECTOPLASM_RUSTS_NETHERITE.get()) {
            this.checkFreeze(level, pos);
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean bl) {
        super.neighborChanged(state, level, pos, block, fromPos, bl);
        if (JNEConfigs.ECTOPLASM_RUSTS_NETHERITE.get()) {
            this.checkFreeze(level, pos);
        }
    }

    public void checkFreeze(Level level, BlockPos pos) {
        initFreeze();
        Direction[] faces = Direction.values();
        for (Direction direction : faces) {
            BlockPos offset = pos.offset(direction.getNormal());
            BlockState oldState = level.getBlockState(offset);
            if (FREEZES.containsKey(oldState.getBlock())) {
                BlockState newState = FREEZES.get(oldState.getBlock()).defaultBlockState();
                level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), JNESoundEvents.SOUL_SLATE_SOLIDIFYING.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
                for (Property property : oldState.getProperties()) {
                    newState = newState.hasProperty(property) ? newState.setValue(property, oldState.getValue(property)) : newState;
                }
                level.setBlock(offset, newState, 2);
            }
        }
    }

    private static void initFreeze() {
        if (FREEZES != null) {
            return;
        }
        FREEZES = Util.make(Maps.newHashMap(), (map) -> {
            map.put(JNEBlocks.NETHERITE_PLATED_BLOCK.get(), JNEBlocks.RUSTY_NETHERITE_PLATED_BLOCK.get());
            map.put(JNEBlocks.NETHERITE_GRATE.get(), JNEBlocks.RUSTY_NETHERITE_GRATE.get());
            map.put(JNEBlocks.CUT_NETHERITE_BLOCK.get(), JNEBlocks.RUSTY_CUT_NETHERITE_BLOCK.get());
            map.put(JNEBlocks.CUT_NETHERITE_SLAB.get(), JNEBlocks.RUSTY_CUT_NETHERITE_SLAB.get());
            map.put(JNEBlocks.CUT_NETHERITE_STAIRS.get(), JNEBlocks.RUSTY_CUT_NETHERITE_STAIRS.get());
            map.put(JNEBlocks.CUT_NETHERITE_PILLAR.get(), JNEBlocks.RUSTY_CUT_NETHERITE_PILLAR.get());
        });
    }
}
