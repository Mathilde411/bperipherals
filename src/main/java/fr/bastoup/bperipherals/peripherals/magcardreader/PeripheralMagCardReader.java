package fr.bastoup.bperipherals.peripherals.magcardreader;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import fr.bastoup.bperipherals.util.Config;
import fr.bastoup.bperipherals.util.Util;
import fr.bastoup.bperipherals.util.peripherals.BPeripheral;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.ByteBuffer;

public class PeripheralMagCardReader extends BPeripheral {

    public static final String TYPE = "mag_card_reader";

    public PeripheralMagCardReader(TileMagCardReader tile) {
        super(tile);
    }

    private TileMagCardReader getTile() {
        return ((TileMagCardReader) tile);
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

    @LuaFunction
    public final void write(ByteBuffer bytes, String label) throws LuaException {
        byte[] data = Util.getByteBufferArray(bytes);
        if (data.length > Config.MAX_MAG_CARD_DATA)
            throw new LuaException("You can't put more than " + Config.MAX_MAG_CARD_DATA + " characters in a mag card.");
        getTile().writeCard(data, label);
    }

    @LuaFunction
    public final void cancelWrite() {
        getTile().cancelWrite();
    }

    @LuaFunction
    public final void lightGreen() {
        getTile().setState(BlockStateMagCardReader.READ);
    }

    @LuaFunction
    public final void lightYellow() {
        getTile().setState(BlockStateMagCardReader.WAIT);
    }

    @LuaFunction
    public final void lightRed() {
        getTile().setState(BlockStateMagCardReader.WRONG);
    }
}
