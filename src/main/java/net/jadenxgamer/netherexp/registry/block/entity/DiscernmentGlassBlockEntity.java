package net.jadenxgamer.netherexp.registry.block.entity;

import net.jadenxgamer.netherexp.registry.block.JNEBlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class DiscernmentGlassBlockEntity extends BlockEntity {
    private static final String FILTER_ITEM_TAG = "FilterItem";
    private ItemStack filterItem = ItemStack.EMPTY;

    public DiscernmentGlassBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(JNEBlockEntityType.DISCERNMENT_GLASS.get(), pPos, pBlockState);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        if (!this.getFilterItem().isEmpty()) {
            nbt.put(FILTER_ITEM_TAG, this.getFilterItem().save(new CompoundTag()));
        }
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.setFilterItem(ItemStack.of(nbt.getCompound(FILTER_ITEM_TAG)));
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = new CompoundTag();
        if (!this.getFilterItem().isEmpty()) {
            nbt.put(FILTER_ITEM_TAG, this.getFilterItem().save(new CompoundTag()));
        }
        return nbt;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull BlockEntityType<?> getType() {
        return JNEBlockEntityType.DISCERNMENT_GLASS.get();
    }

    public ItemStack getFilterItem() {
        return this.filterItem;
    }

    public void setFilterItem(ItemStack stack) {
        if (!stack.isEmpty()) {
            stack = stack.copyWithCount(1);
        }
        this.filterItem = stack;
    }

    public void removeFilterItem() {
        this.filterItem = ItemStack.EMPTY;
    }
}
