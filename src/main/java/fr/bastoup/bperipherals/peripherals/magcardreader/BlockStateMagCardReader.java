package fr.bastoup.bperipherals.peripherals.magcardreader;

import net.minecraft.util.IStringSerializable;

public enum BlockStateMagCardReader implements IStringSerializable {
    WRITE,
    WAITING,
    CORRECT,
    WRONG;

    @Override
    public String getString() {
        return super.name().toLowerCase();
    }
}
