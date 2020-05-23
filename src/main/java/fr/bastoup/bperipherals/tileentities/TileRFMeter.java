package fr.bastoup.bperipherals.tileentities;

import fr.bastoup.bperipherals.capabilites.RFMeterEnergyIn;
import fr.bastoup.bperipherals.capabilites.RFMeterEnergyOut;
import fr.bastoup.bperipherals.init.ModTileTypes;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileRFMeter extends TileOrientable implements ICapabilityProvider, ITickable{

	private static final String[] METHODS = new String[] {"getEnergyStored", "getMaxEnergyStored", "getEnergyTransferedLastTick", "getTransferRate", "getMaxTransferRate", "setTransferRate"};

	private final RFMeterEnergyOut outEnergyStrorge = new RFMeterEnergyOut(this);
	private final RFMeterEnergyIn inEnergyStrorage = new RFMeterEnergyIn(outEnergyStrorge);
	private final LazyOptional<IEnergyStorage> holderOut = LazyOptional.of(() -> outEnergyStrorge);
	private final LazyOptional<IEnergyStorage> holderIn = LazyOptional.of(() -> inEnergyStrorage);

	private int energyTransferedLastTick = 0;
	private int energyStoredLastTick = 0;

	public TileRFMeter() {
		super(ModTileTypes.RF_METER);
	}

	@Override
	public CompoundNBT getUpdateTag() {
		CompoundNBT nbt = new CompoundNBT();
		nbt.put("Energy", outEnergyStrorge.serializeNBT());
		nbt.putInt("energyTransfered", energyTransferedLastTick);
		return nbt;
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		CompoundNBT nbt = pkt.getNbtCompound();
		outEnergyStrorge.deserializeNBT(nbt.getCompound("Energy"));
		energyTransferedLastTick = nbt.getInt("energyTransfered");
	}
	@Override
	public void read(CompoundNBT nbt) {
		super.read(nbt);
		outEnergyStrorge.deserializeNBT(nbt.getCompound("Energy"));
		energyTransferedLastTick = nbt.getInt("energyTransfered");
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		CompoundNBT nbt = super.write(compound);
		nbt.put("Energy", outEnergyStrorge.serializeNBT());
		nbt.putInt("energyTransfered", energyTransferedLastTick);
		return nbt;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {
		if (capability.equals(CapabilityEnergy.ENERGY)) {
			switch (getFaceOfFacing(facing)) {
			case LEFT:
				return (LazyOptional<T>) holderIn;
			case RIGHT:
				return (LazyOptional<T>) holderOut;
			default:
				return null;
			}
		} else {
			return super.getCapability(capability, facing);
		}
	}

	@Override
	public void tick() {
		if (!world.isRemote) {
			energyStoredLastTick = outEnergyStrorge.getEnergyStored();
			outEnergyStrorge.sendEnergy();
			energyTransferedLastTick = energyStoredLastTick - outEnergyStrorge.getEnergyStored();
			
			if(outEnergyStrorge.resetUpdated()) {
				world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2);
			}
		}
	}

	public int getEnergyTransferedLastTick() {
		return energyTransferedLastTick;
	}

	public int getEnergyStored() {
		return outEnergyStrorge.getEnergyStored();
	}

	public int getMaxEnergyStored() {
		return outEnergyStrorge.getMaxEnergyStored();
	}

	public int getTransferRate() {
		return outEnergyStrorge.getTransferRate();
	}

	public int getMaxTransferRate() {
		return outEnergyStrorge.getMaxTransferRate();
	}

	public void setTransferRate(int rate){
		outEnergyStrorge.setTransferRate(rate);
	}
}
