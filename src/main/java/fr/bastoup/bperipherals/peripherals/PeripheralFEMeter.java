package fr.bastoup.bperipherals.peripherals;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import fr.bastoup.bperipherals.tileentities.TileFEMeter;

public class PeripheralFEMeter implements IPeripheral {

    public static final String TYPE = "fe_meter";

    private final TileFEMeter tile;

    public PeripheralFEMeter(TileFEMeter tile) {
        this.tile = tile;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public void attach(IComputerAccess computer) {
    }

    @Override
    public void detach(IComputerAccess computer) {
    }

    @Override
    public Object getTarget() {
        return tile;
    }

    @Override
    public boolean equals(IPeripheral other) {
        return other instanceof PeripheralFEMeter && ((TileFEMeter) other.getTarget()).getWorld().equals(tile.getWorld()) &&
                ((TileFEMeter) other.getTarget()).getPos().equals(tile.getPos());
    }

    @LuaFunction
    public final int getEnergyTransferedLastTick() {
        return tile.getEnergyTransferedLastTick();
    }

    @LuaFunction
    public final int getEnergyStored() {
        return tile.getEnergyStored();
    }

    @LuaFunction
    public final int getMaxEnergyStored() {
        return tile.getMaxEnergyStored();
    }

    @LuaFunction
    public final int getTransferRate() {
        return tile.getTransferRate();
    }

    @LuaFunction
    public final void setTransferRate(int rate) {
        tile.setTransferRate(rate);
    }

    @LuaFunction
    public final int getMaxTransferRate() {
        return tile.getMaxTransferRate();
    }
}
