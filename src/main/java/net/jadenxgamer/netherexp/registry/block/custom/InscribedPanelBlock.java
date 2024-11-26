package net.jadenxgamer.netherexp.registry.block.custom;

import net.jadenxgamer.netherexp.registry.block.custom.enums.EctoplasmWaterlogged;
import net.jadenxgamer.netherexp.registry.fluid.JNEFluids;
import net.jadenxgamer.netherexp.registry.misc_registry.JNESoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.ToIntFunction;

public class InscribedPanelBlock extends Block implements SimpleEctoplasmWaterloggedBlock {
    public static final EnumProperty<EctoplasmWaterlogged> LIQUIDLOGGED = EnumProperty.create("liquidlogged", EctoplasmWaterlogged.class);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final IntegerProperty INSCRIPTION = IntegerProperty.create("inscription", 0, 28);
    public static final BooleanProperty SALTED = BooleanProperty.create("salted");

    public static final VoxelShape NORTH_SHAPE =  Block.box(1, 2, 14, 15, 16, 16);
    public static final VoxelShape EAST_SHAPE =  Block.box(0, 2, 1, 2, 16, 15);
    public static final VoxelShape SOUTH_SHAPE =  Block.box(1, 2, 0, 15, 16, 2);
    public static final VoxelShape WEST_SHAPE =  Block.box(14, 2, 1, 16, 16, 15);

    public static final ToIntFunction<BlockState> STATE_TO_LUMINANCE = state -> state.getValue(LIQUIDLOGGED) == EctoplasmWaterlogged.ECTOPLASM ? 15 : 0;

    public InscribedPanelBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(LIQUIDLOGGED, EctoplasmWaterlogged.AIR).setValue(INSCRIPTION, 0).setValue(SALTED, false).setValue(FACING, Direction.NORTH));
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case EAST -> EAST_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
                default -> NORTH_SHAPE;
        };
    }


    @Override
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!state.getValue(SALTED)) {
            if (player.getItemInHand(hand).is(Items.HONEYCOMB)) {
                level.setBlock(pos, state.setValue(SALTED, true), UPDATE_ALL);
                spawnParticles(level, pos, ParticleTypes.WAX_ON);
                level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.HONEYCOMB_WAX_ON, SoundSource.BLOCKS, 1.0f, 1.0f);
                return InteractionResult.SUCCESS;
            } else {
                level.setBlock(pos, state.cycle(INSCRIPTION), UPDATE_ALL);
                spawnParticles(level, pos, ParticleTypes.SOUL);
                level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), JNESoundEvents.BLOCK_POLISHED_BLACKSTONE_BRICKS_HIT.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        if (fluidState.getType() == Fluids.WATER) {
            return this.defaultBlockState().setValue(LIQUIDLOGGED, EctoplasmWaterlogged.WATER).setValue(FACING, context.getHorizontalDirection().getOpposite());
        } else if (fluidState.getType() == JNEFluids.ECTOPLASM_SOURCE.get()) {
            return this.defaultBlockState().setValue(LIQUIDLOGGED, EctoplasmWaterlogged.ECTOPLASM).setValue(FACING, context.getHorizontalDirection().getOpposite());
        } else return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull FluidState getFluidState(BlockState state) {
        if (state.getValue(LIQUIDLOGGED) == EctoplasmWaterlogged.WATER) {
            return Fluids.WATER.getSource(true);
        } else if (state.getValue(LIQUIDLOGGED) == EctoplasmWaterlogged.ECTOPLASM) {
            return JNEFluids.ECTOPLASM_SOURCE.get().getSource(true);
        } else {
            return super.getFluidState(state);
        }
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if ((state.getValue(LIQUIDLOGGED) == EctoplasmWaterlogged.WATER)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        } else if ((state.getValue(LIQUIDLOGGED) == EctoplasmWaterlogged.ECTOPLASM)) {
            level.scheduleTick(pos, JNEFluids.ECTOPLASM_SOURCE.get(), JNEFluids.ECTOPLASM_SOURCE.get().getTickDelay(level));
        }

        return state;
    }

    private static void spawnParticles(Level level, BlockPos blockPos, ParticleOptions particle) {
        RandomSource randomSource = level.random;
        Direction[] var5 = Direction.values();

        for (Direction direction : var5) {
            BlockPos blockPos2 = blockPos.relative(direction);
            if (!level.getBlockState(blockPos2).isSolidRender(level, blockPos2)) {
                Direction.Axis axis = direction.getAxis();
                double e = axis == Direction.Axis.X ? 0.5 + 0.5625 * (double) direction.getStepX() : (double) randomSource.nextFloat();
                double f = axis == Direction.Axis.Y ? 0.5 + 0.5625 * (double) direction.getStepY() : (double) randomSource.nextFloat();
                double g = axis == Direction.Axis.Z ? 0.5 + 0.5625 * (double) direction.getStepZ() : (double) randomSource.nextFloat();
                level.addParticle(particle, (double) blockPos.getX() + e, (double) blockPos.getY() + f, (double) blockPos.getZ() + g, 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIQUIDLOGGED, FACING, INSCRIPTION, SALTED);
    }
}
