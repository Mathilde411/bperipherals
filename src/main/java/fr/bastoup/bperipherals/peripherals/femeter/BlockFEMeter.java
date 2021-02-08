package fr.bastoup.bperipherals.peripherals.femeter;

import fr.bastoup.bperipherals.init.ModTileTypes;
import fr.bastoup.bperipherals.util.blocks.BlockPeripheral;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class BlockFEMeter extends BlockPeripheral {

    public BlockFEMeter() {
        super(Properties.create(Material.ROCK).hardnessAndResistance(2.0F), "fe_meter");
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModTileTypes.FE_METER.create();
    }
}
