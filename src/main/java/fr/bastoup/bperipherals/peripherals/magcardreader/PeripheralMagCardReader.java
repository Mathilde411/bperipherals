package fr.bastoup.bperipherals.peripherals.magcardreader;

import dan200.computercraft.api.peripheral.IPeripheral;
import fr.bastoup.bperipherals.util.peripherals.BPeripheral;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PeripheralMagCardReader extends BPeripheral {

    public static final String TYPE = "mag_card_reader";

    public PeripheralMagCardReader(TileMagCardReader tile) {
        super(tile);
    }

    @Nonnull
    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return other instanceof PeripheralMagCardReader && ((TileMagCardReader) other.getTarget()).getWorld().equals(tile.getWorld()) &&
                ((TileMagCardReader) other.getTarget()).getPos().equals(tile.getPos());
    }
}
