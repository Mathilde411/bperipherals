package fr.bastoup.bperipherals.tileentities;

import dan200.computercraft.api.peripheral.IPeripheral;
import fr.bastoup.bperipherals.init.ModTileTypes;
import fr.bastoup.bperipherals.peripherals.PeripheralCryptographicAccelerator;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntityType;

public class TileCryptographicAccelerator extends TileOrientable implements TilePeripheral {
    public TileCryptographicAccelerator() {
        super(ModTileTypes.CRYPTOGRAPHIC_ACCELERATOR);
    }

    @Override
    public IPeripheral getPeripheral() {
        return new PeripheralCryptographicAccelerator(this);
    }
}
