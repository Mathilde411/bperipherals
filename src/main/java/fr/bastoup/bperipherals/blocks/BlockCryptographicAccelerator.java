package fr.bastoup.bperipherals.blocks;

import fr.bastoup.bperipherals.init.ModTileTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BlockCryptographicAccelerator extends BlockOrientable{

    public BlockCryptographicAccelerator() {
        super(Properties.create(Material.ROCK).hardnessAndResistance(2.0F), "cryptographic_accelerator");

        setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModTileTypes.CRYPTOGRAPHIC_ACCELERATOR.create();
    }

}
