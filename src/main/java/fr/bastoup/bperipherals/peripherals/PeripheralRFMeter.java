package fr.bastoup.bperipherals.peripherals;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import fr.bastoup.bperipherals.tileentities.TileFEMeter;

public class PeripheralRFMeter implements IPeripheral {

    public static final String TYPE = "fe_meter";

    private final TileFEMeter tile;

    public PeripheralRFMeter(TileFEMeter tile) {
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
        return other instanceof PeripheralRFMeter && ((TileFEMeter) other.getTarget()).getWorld().equals(tile.getWorld()) &&
                ((TileFEMeter) other.getTarget()).getPos().equals(tile.getPos());
    }

    @LuaFunction
    public int getEnergyTransferedLastTick() {
        return tile.getEnergyTransferedLastTick();
    }

    @LuaFunction
    public int getEnergyStored() {
        return tile.getEnergyStored();
    }

    @LuaFunction
    public int getMaxEnergyStored() {
        return tile.getMaxEnergyStored();
    }

    @LuaFunction
    public int getTransferRate() {
        return tile.getTransferRate();
    }

    @LuaFunction
    public void setTransferRate(int rate) {
        tile.setTransferRate(rate);
    }

    @LuaFunction
    public int getMaxTransferRate() {
        return tile.getMaxTransferRate();
    }
}
