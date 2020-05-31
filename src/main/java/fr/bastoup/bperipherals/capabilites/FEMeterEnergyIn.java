package fr.bastoup.bperipherals.capabilites;

import net.minecraftforge.energy.IEnergyStorage;

public class FEMeterEnergyIn implements IEnergyStorage {

    private final FEMeterEnergyOut outStorage;

    public FEMeterEnergyIn(FEMeterEnergyOut out) {
        outStorage = out;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return outStorage.transferFromIn(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
		return 0;
	}

	@Override
	public int getEnergyStored() {
		return 0;
	}

	@Override
	public int getMaxEnergyStored() {
		return 0;
	}

	@Override
	public boolean canExtract() {
		return false;
	}

	@Override
	public boolean canReceive() {
		return true;
	}
	
	

}
