package fr.bastoup.bperipherals.capabilites;

import fr.bastoup.bperipherals.tileentities.TileRFMeter;
import fr.bastoup.bperipherals.util.BlockFaces;
import fr.bastoup.bperipherals.util.Util;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class RFMeterEnergyOut implements IEnergyStorage, INBTSerializable<CompoundNBT>{
	
	public final static int RF_METER_CAPACITY = 64000;
	public final static int MAX_TRANSFER_RATE = 32000;
	
	private int energyStored;
	private int transferRate;
	private TileRFMeter tile;
	private boolean updated = false;
	
	public RFMeterEnergyOut(TileRFMeter tileRFMeter) {
		energyStored = 0;
		transferRate = 1000;
		tile = tileRFMeter;
	}
	
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		return 0;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		return 0;
	}

	@Override
	public int getEnergyStored() {
		return energyStored;
	}

	@Override
	public int getMaxEnergyStored() {
		return RF_METER_CAPACITY;
	}

	@Override
	public boolean canExtract() {
		return false;
	}

	@Override
	public boolean canReceive() {
		return false;
	}

	public int getTransferRate() {
		return transferRate;
	}

	public void setTransferRate(int transferRate) {
		int actualTransferRate;
		if(transferRate > MAX_TRANSFER_RATE) {
			actualTransferRate = MAX_TRANSFER_RATE;
		} else if (transferRate < 0) {
			actualTransferRate = 0;
		} else {
			actualTransferRate = transferRate;
		}
		
		this.transferRate = actualTransferRate;
		updated = true;
		tile.markDirty();
	}
	
	public int transferFromIn(int maxTransfer, boolean simulate) {
		int actualTransfer = 0;
		if(maxTransfer >= transferRate) {
			actualTransfer = transferRate;
		} else {
			actualTransfer = maxTransfer;
		}
		
		if(maxTransfer + energyStored >= RF_METER_CAPACITY) {
			actualTransfer = RF_METER_CAPACITY - energyStored;
		}
		
		if(!simulate) {
			energyStored += actualTransfer;
			updated = true;
			tile.markDirty();
		}
		
		return actualTransfer;
	}
	
	public int getExtractableEnergy() {
		if(energyStored < transferRate) {
			return energyStored;
		} else {
			return transferRate;
		}
	}
	
	public void sendEnergy() {
		Direction rightFace = tile.getFacingOfFace(BlockFaces.RIGHT);
		BlockPos pos = Util.getNextPos(tile.getPos(), rightFace);
		TileEntity targetTile = tile.getWorld().getTileEntity(pos);
		if(targetTile != null && targetTile instanceof ICapabilityProvider && ((ICapabilityProvider) targetTile).getCapability(CapabilityEnergy.ENERGY, Util.getOppositeFacing(rightFace)).isPresent()) {
			IEnergyStorage cap = ((ICapabilityProvider) targetTile).getCapability(CapabilityEnergy.ENERGY, Util.getOppositeFacing(rightFace)).orElse(null);
			if(cap.canReceive()) {
				int extracted = cap.receiveEnergy(getExtractableEnergy(), false);
				energyStored -= extracted;
				updated = true;
				tile.markDirty();
			}
		}
	}
	
	public int getMaxTransferRate() {
		return MAX_TRANSFER_RATE;
	}
	
	public boolean resetUpdated() {
		if(updated) {
			updated = false;
			return true;
		}
		return false;
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putInt("transferRate", transferRate);
		nbt.putInt("energyStored", energyStored);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		transferRate = nbt.getInt("transferRate");
		energyStored = nbt.getInt("energyStored");
	}
}
