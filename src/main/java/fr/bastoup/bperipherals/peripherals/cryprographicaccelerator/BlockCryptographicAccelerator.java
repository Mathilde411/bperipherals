package fr.bastoup.bperipherals.peripherals.cryprographicaccelerator;

import fr.bastoup.bperipherals.init.ModTileTypes;
import fr.bastoup.bperipherals.util.blocks.BlockPeripheral;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BlockCryptographicAccelerator extends BlockPeripheral {

    public BlockCryptographicAccelerator() {
        super(Properties.of(Material.STONE).strength(2.0F), "cryptographic_accelerator");
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
