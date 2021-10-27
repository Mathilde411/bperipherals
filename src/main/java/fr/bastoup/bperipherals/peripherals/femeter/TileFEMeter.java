package fr.bastoup.bperipherals.peripherals.femeter;

import fr.bastoup.bperipherals.init.ModTileTypes;
import fr.bastoup.bperipherals.util.tiles.TilePeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;

public class TileFEMeter extends TilePeripheral {

    private final EnergyFEMeterOut outEnergyStorage = new EnergyFEMeterOut(this);
    private final EnergyFEMeterIn inEnergyStrorage = new EnergyFEMeterIn(outEnergyStorage);

    private final LazyOptional<EnergyFEMeterOut> holderOut = LazyOptional.of(() -> outEnergyStorage);
    private final LazyOptional<EnergyFEMeterIn> holderIn = LazyOptional.of(() -> inEnergyStrorage);

    private int energyTransferedLastTick = 0;
    private int energyStoredLastTick = 0;

    public TileFEMeter(BlockPos pos, BlockState state) {
        super(ModTileTypes.FE_METER, pos, state);
        this.setPeripheral(new PeripheralFEMeter(this));
    }

    @Nonnull
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = new CompoundTag();
        nbt.put("Energy", holderOut.orElse(null).serializeNBT());
        nbt.putInt("energyTransfered", energyTransferedLastTick);
        return nbt;
    }

	@Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag nbt = pkt.getTag();
        holderOut.orElse(null).deserializeNBT(nbt.getCompound("Energy"));
        energyTransferedLastTick = nbt.getInt("energyTransfered");
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        holderOut.orElse(null).deserializeNBT(nbt.getCompound("Energy"));
        energyTransferedLastTick = nbt.getInt("energyTransfered");
    }

    @Nonnull
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = super.serializeNBT();
        nbt.put("Energy", holderOut.orElse(null).serializeNBT());
        nbt.putInt("energyTransfered", energyTransferedLastTick);
        return nbt;
    }

    @Override
    public CompoundTag save(CompoundTag nbtParam) {
        CompoundTag nbt = super.save(nbtParam);
        nbt.put("Energy", holderOut.orElse(null).serializeNBT());
        nbt.putInt("energyTransfered", energyTransferedLastTick);
        return nbt;
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        holderOut.orElse(null).deserializeNBT(nbt.getCompound("Energy"));
        energyTransferedLastTick = nbt.getInt("energyTransfered");
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {

        if (capability.equals(CapabilityEnergy.ENERGY)) {
            switch (getFaceOfFacing(facing)) {
                case LEFT:
                    return holderIn.cast();
                case RIGHT:
                    return holderOut.cast();
                default:
                    return null;
            }
        }

        return super.getCapability(capability, facing);
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState blockState, T tile) {
        if(!(tile instanceof TileFEMeter))
            return;

        TileFEMeter blockEntity = (TileFEMeter) tile;
        if (!blockEntity.getLevel().isClientSide) {
            blockEntity.energyStoredLastTick = blockEntity.holderOut.orElse(null).getEnergyStored();
            blockEntity.holderOut.orElse(null).sendEnergy();
            blockEntity.energyTransferedLastTick = blockEntity.energyStoredLastTick - blockEntity.holderOut.orElse(null).getEnergyStored();

            if (blockEntity.holderOut.orElse(null).resetUpdated()) {
                blockEntity.getLevel().sendBlockUpdated(blockEntity.worldPosition, blockEntity.getLevel().getBlockState(blockEntity.worldPosition), blockEntity.getLevel().getBlockState(blockEntity.worldPosition), 2);
            }
        }
    }

    public int getEnergyTransferedLastTick() {
        return energyTransferedLastTick;
    }

    public int getEnergyStored() {
        return holderOut.orElse(null).getEnergyStored();
    }

    public int getMaxEnergyStored() {
        return holderOut.orElse(null).getMaxEnergyStored();
    }

    public int getTransferRate() {
        return holderOut.orElse(null).getTransferRate();
    }

    public void setTransferRate(int rate) {
        holderOut.orElse(null).setTransferRate(rate);
    }

    public int getMaxTransferRate() {
        return holderOut.orElse(null).getMaxTransferRate();
    }


}
