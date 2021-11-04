package fr.bastoup.bperipherals.peripherals.femeter;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import fr.bastoup.bperipherals.util.peripherals.BPeripheral;

public class PeripheralFEMeter extends BPeripheral {

    public static final String TYPE = "fe_meter";

    public PeripheralFEMeter(BlockEntityFEMeter tile) {
        super(tile);
    }

    private BlockEntityFEMeter getTile() {
        return ((BlockEntityFEMeter) blockEntity);
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public boolean equals(IPeripheral other) {
        return other instanceof PeripheralFEMeter && ((BlockEntityFEMeter) other.getTarget()).getLevel().equals(blockEntity.getLevel()) &&
                ((BlockEntityFEMeter) other.getTarget()).getBlockPos().equals(blockEntity.getBlockPos());
    }

    @LuaFunction
    public final int getEnergyTransferedLastTick() {
        return getTile().getEnergyTransferedLastTick();
    }

    @LuaFunction
    public final int getEnergyStored() {
        return getTile().getEnergyStored();
    }

    @LuaFunction
    public final int getMaxEnergyStored() {
        return getTile().getMaxEnergyStored();
    }

    @LuaFunction
    public final int getTransferRate() {
        return getTile().getTransferRate();
    }

    @LuaFunction
    public final void setTransferRate(int rate) {
        getTile().setTransferRate(rate);
    }

    @LuaFunction
    public final int getMaxTransferRate() {
        return getTile().getMaxTransferRate();
    }
}
