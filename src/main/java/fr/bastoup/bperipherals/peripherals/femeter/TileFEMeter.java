package fr.bastoup.bperipherals.peripherals.femeter;

import fr.bastoup.bperipherals.init.ModTileTypes;
import fr.bastoup.bperipherals.util.tiles.TilePeripheral;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;

public class TileFEMeter extends TilePeripheral implements ITickableTileEntity {

    private final EnergyFEMeterOut outEnergyStorage = new EnergyFEMeterOut(this);
    private final EnergyFEMeterIn inEnergyStrorage = new EnergyFEMeterIn(outEnergyStorage);

    private final LazyOptional<EnergyFEMeterOut> holderOut = LazyOptional.of(() -> outEnergyStorage);
    private final LazyOptional<EnergyFEMeterIn> holderIn = LazyOptional.of(() -> inEnergyStrorage);

    private int energyTransferedLastTick = 0;
    private int energyStoredLastTick = 0;

    public TileFEMeter() {
        super(ModTileTypes.FE_METER);
        this.setPeripheral(new PeripheralFEMeter(this));
    }

    @Nonnull
    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("Energy", holderOut.orElse(null).serializeNBT());
        nbt.putInt("energyTransfered", energyTransferedLastTick);
        return nbt;
    }

	@Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT nbt = pkt.getTag();
        holderOut.orElse(null).deserializeNBT(nbt.getCompound("Energy"));
        energyTransferedLastTick = nbt.getInt("energyTransfered");
    }

    @Override
    public void deserializeNBT(BlockState state, CompoundNBT nbt) {
        super.deserializeNBT(state, nbt);
        holderOut.orElse(null).deserializeNBT(nbt.getCompound("Energy"));
        energyTransferedLastTick = nbt.getInt("energyTransfered");
    }

    @Nonnull
    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = super.serializeNBT();
        nbt.put("Energy", holderOut.orElse(null).serializeNBT());
        nbt.putInt("energyTransfered", energyTransferedLastTick);
        return nbt;
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

	@Override
	public void tick() {
        if (!this.getLevel().isClientSide) {
            energyStoredLastTick = holderOut.orElse(null).getEnergyStored();
            holderOut.orElse(null).sendEnergy();
            energyTransferedLastTick = energyStoredLastTick - holderOut.orElse(null).getEnergyStored();

            if (holderOut.orElse(null).resetUpdated()) {
                this.getLevel().sendBlockUpdated(worldPosition, this.getLevel().getBlockState(worldPosition), this.getLevel().getBlockState(worldPosition), 2);
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
