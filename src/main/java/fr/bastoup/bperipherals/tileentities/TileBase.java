package fr.bastoup.bperipherals.tileentities;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public abstract class TileBase extends TileEntity {

    public TileBase(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }
}
