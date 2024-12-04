package net.jadenxgamer.netherexp.registry.block.custom;

import net.jadenxgamer.netherexp.registry.block.JNEBlocks;
import net.jadenxgamer.netherexp.registry.block.custom.enums.Liquidlogged;
import net.jadenxgamer.netherexp.registry.fluid.JNEFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LiquidloggedGrateBlock extends Block implements SimpleWaterloggedBlock {

    public static final EnumProperty<Liquidlogged> LIQUIDLOGGED = EnumProperty.create("liquidlogged", Liquidlogged.class);

    public LiquidloggedGrateBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(LIQUIDLOGGED, Liquidlogged.AIR));
    }

    public static int getLuminance(BlockState state) {
        switch (state.getValue(LIQUIDLOGGED)) {
            default -> {
                return 0;
            }
            case LAVA -> {
                return 15;
            }
            case ECTOPLASM -> {
                return 7;
            }
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        if (fluidState.getType() == Fluids.WATER) {
            return this.defaultBlockState().setValue(LIQUIDLOGGED, Liquidlogged.WATER);
        }
        else if (fluidState.getType() == Fluids.LAVA) {
            return this.defaultBlockState().setValue(LIQUIDLOGGED, Liquidlogged.LAVA);
        }
        else if (fluidState.getType() == JNEFluids.ECTOPLASM_SOURCE.get()) {
            if (context.getLevel().getBlockState(context.getClickedPos()).is(JNEBlocks.NETHERITE_GRATE.get())) {
                return JNEBlocks.RUSTY_NETHERITE_GRATE.get().defaultBlockState().setValue(LIQUIDLOGGED, Liquidlogged.ECTOPLASM);
            } else {
                return this.defaultBlockState().setValue(LIQUIDLOGGED, Liquidlogged.ECTOPLASM);
            }
        }
        else return this.defaultBlockState();
    }

    @SuppressWarnings("deprecation")
    public @NotNull VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) {
        return Shapes.empty();
    }

    @SuppressWarnings("deprecation")
    public @NotNull VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public boolean canPlaceLiquid(BlockGetter level, BlockPos pos, BlockState state, Fluid fluid) {
        return fluid == Fluids.WATER || fluid == Fluids.LAVA || fluid == JNEFluids.ECTOPLASM_SOURCE.get();
    }

    @Override
    public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
        if (state.getValue(LIQUIDLOGGED) == Liquidlogged.AIR) {
            if (!level.isClientSide()) {
                if (fluidState.getType() == Fluids.WATER) {
                    level.setBlock(pos, state.setValue(LIQUIDLOGGED, Liquidlogged.WATER), 3);
                } else if (fluidState.getType() == Fluids.LAVA) {
                    level.setBlock(pos, state.setValue(LIQUIDLOGGED, Liquidlogged.LAVA), 3);
                } else if (fluidState.getType() == JNEFluids.ECTOPLASM_SOURCE.get()) {
                    level.setBlock(pos, state.setValue(LIQUIDLOGGED, Liquidlogged.ECTOPLASM), 3);
                }
                level.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(level));
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public @NotNull ItemStack pickupBlock(LevelAccessor level, BlockPos pos, BlockState state) {
        if (state.getValue(LIQUIDLOGGED) != Liquidlogged.AIR) {
            level.setBlock(pos, state.setValue(LIQUIDLOGGED, Liquidlogged.AIR), 3);
            if (!state.canSurvive(level, pos)) {
                level.destroyBlock(pos, true);
            }
            if (state.getValue(LIQUIDLOGGED) == Liquidlogged.WATER) {
                return new ItemStack(Items.WATER_BUCKET);
            }
            else if (state.getValue(LIQUIDLOGGED) == Liquidlogged.LAVA) {
                return new ItemStack(Items.LAVA_BUCKET);
            }
            else {
                return new ItemStack(JNEFluids.ECTOPLASM_BUCKET.get());
            }
        } else {
            return ItemStack.EMPTY;
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull FluidState getFluidState(BlockState state) {
        if (state.getValue(LIQUIDLOGGED) == Liquidlogged.WATER) {
            return Fluids.WATER.getSource(false);
        }
        else if (state.getValue(LIQUIDLOGGED) == Liquidlogged.LAVA) {
            return Fluids.LAVA.getSource(false);
        }
        else if (state.getValue(LIQUIDLOGGED) == Liquidlogged.ECTOPLASM) {
            return JNEFluids.ECTOPLASM_SOURCE.get().getSource(false);
        }
        else {
            return super.getFluidState(state);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if ((state.getValue(LIQUIDLOGGED) == Liquidlogged.WATER)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        else if ((state.getValue(LIQUIDLOGGED) == Liquidlogged.LAVA)) {
            level.scheduleTick(pos, Fluids.LAVA, Fluids.LAVA.getTickDelay(level));
        }
        else if ((state.getValue(LIQUIDLOGGED) == Liquidlogged.ECTOPLASM)) {
            level.scheduleTick(pos, JNEFluids.ECTOPLASM_SOURCE.get(), JNEFluids.ECTOPLASM_SOURCE.get().getTickDelay(level));
        }

        return state;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIQUIDLOGGED);
    }
}