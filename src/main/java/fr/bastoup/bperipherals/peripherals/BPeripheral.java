package fr.bastoup.bperipherals.peripherals;

import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class BPeripheral implements IPeripheral {

    protected BlockEntityPeripheral blockEntity;

    public BPeripheral(BlockEntityPeripheral tile) {
        this.blockEntity = tile;
    }

    @Override
    public void attach(@Nonnull IComputerAccess computer) {
        blockEntity.addComputer(computer);
    }

    @Override
    public void detach(@Nonnull IComputerAccess computer) {
        blockEntity.removeComputer(computer);
    }

    @Nullable
    @Override
    public Object getTarget() {
        return blockEntity;
    }
}
