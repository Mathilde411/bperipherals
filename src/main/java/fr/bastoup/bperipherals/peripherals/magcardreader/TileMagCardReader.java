package fr.bastoup.bperipherals.peripherals.magcardreader;

import dan200.computercraft.api.peripheral.IComputerAccess;
import fr.bastoup.bperipherals.init.ModTileTypes;
import fr.bastoup.bperipherals.util.tiles.TilePeripheral;

public class TileMagCardReader extends TilePeripheral {
    public TileMagCardReader() {
        super(ModTileTypes.MAG_CARD_READER);
        this.setPeripheral(new PeripheralMagCardReader(this));
    }

    public void magSwipe(byte[] data) {
        synchronized (computers) {
            for (IComputerAccess computer : computers) {
                computer.queueEvent("mag_swipe", computer.getAttachmentName(), data);
            }
        }
    }


}
