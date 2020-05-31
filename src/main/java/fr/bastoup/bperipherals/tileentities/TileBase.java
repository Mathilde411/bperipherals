package fr.bastoup.bperipherals.tileentities;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;

public abstract class TileBase extends TileEntity {

    public TileBase(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public void destroy() {

    }

    public ActionResultType onActivate(PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        return ActionResultType.PASS;
    }
}
