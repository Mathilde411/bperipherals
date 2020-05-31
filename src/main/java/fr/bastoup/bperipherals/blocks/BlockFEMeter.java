package fr.bastoup.bperipherals.blocks;

import fr.bastoup.bperipherals.init.ModTileTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockReader;

public class BlockFEMeter extends BlockOrientable {

    public BlockFEMeter() {
        super(Properties.create(Material.ROCK).hardnessAndResistance(2.0F), "fe_meter");

        setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH));
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
