package fr.bastoup.bperipherals.util.peripherals;

import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import fr.bastoup.bperipherals.util.tiles.TilePeripheral;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class BPeripheral implements IPeripheral {

    protected TilePeripheral tile;

    public BPeripheral(TilePeripheral tile) {
        this.tile = tile;
    }

    @Override
    public void attach(@Nonnull IComputerAccess computer) {
        tile.addComputer(computer);
    }

    @Override
    public void detach(@Nonnull IComputerAccess computer) {
        tile.removeComputer(computer);
    }

    @Nullable
    @Override
    public Object getTarget() {
        return tile;
    }
}
