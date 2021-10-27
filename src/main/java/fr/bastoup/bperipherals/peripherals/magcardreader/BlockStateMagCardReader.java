package fr.bastoup.bperipherals.peripherals.magcardreader;

import net.minecraft.util.StringRepresentable;

public enum BlockStateMagCardReader implements StringRepresentable {
    WRITE,
    WAIT,
    READ,
    WRONG;

    public String getString() {
        return super.name().toLowerCase();
    }

    @Override
    public String getSerializedName() {
        return super.name().toLowerCase();
    }
}
