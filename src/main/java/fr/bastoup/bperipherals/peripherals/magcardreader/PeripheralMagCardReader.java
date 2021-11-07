package fr.bastoup.bperipherals.peripherals.magcardreader;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import fr.bastoup.bperipherals.util.Config;
import fr.bastoup.bperipherals.util.Util;
import fr.bastoup.bperipherals.peripherals.BPeripheral;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.ByteBuffer;
import java.util.Optional;

public class PeripheralMagCardReader extends BPeripheral {

    public static final String TYPE = "mag_card_reader";

    public PeripheralMagCardReader(BlockEntityMagCardReader tile) {
        super(tile);
    }

    private BlockEntityMagCardReader getTile() {
        return ((BlockEntityMagCardReader) blockEntity);
    }

    @Nonnull
    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return other instanceof PeripheralMagCardReader && ((BlockEntityMagCardReader) other.getTarget()).getLevel().equals(blockEntity.getLevel()) &&
                ((BlockEntityMagCardReader) other.getTarget()).getBlockPos().equals(blockEntity.getBlockPos());
    }

    @LuaFunction
    public final void write(ByteBuffer bytes, Optional<String> label) throws LuaException {
        byte[] data = Util.getByteBufferArray(bytes);
        if (data.length > Config.MAX_MAG_CARD_DATA)
            throw new LuaException("You can't put more than " + Config.MAX_MAG_CARD_DATA + " characters in a mag card.");
        getTile().writeCard(data, label.orElse(null));
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
