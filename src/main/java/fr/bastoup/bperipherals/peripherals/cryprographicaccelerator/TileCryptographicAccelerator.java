package fr.bastoup.bperipherals.peripherals.cryprographicaccelerator;

import fr.bastoup.bperipherals.init.ModTileTypes;
import fr.bastoup.bperipherals.util.tiles.TilePeripheral;

public class TileCryptographicAccelerator extends TilePeripheral {
    public TileCryptographicAccelerator() {
        super(ModTileTypes.CRYPTOGRAPHIC_ACCELERATOR);
        this.setPeripheral(new PeripheralCryptographicAccelerator(this));
    }
}
