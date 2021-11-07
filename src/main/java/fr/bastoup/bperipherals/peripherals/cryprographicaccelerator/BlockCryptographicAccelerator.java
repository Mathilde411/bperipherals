package fr.bastoup.bperipherals.peripherals.cryprographicaccelerator;

import fr.bastoup.bperipherals.init.ModTileTypes;
import fr.bastoup.bperipherals.peripherals.BlockPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nullable;

public class BlockCryptographicAccelerator extends BlockPeripheral {

    public BlockCryptographicAccelerator() {
        super(Properties.of(Material.STONE).strength(2.0F), "cryptographic_accelerator");
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModTileTypes.CRYPTOGRAPHIC_ACCELERATOR.create(pos, state);
    }
}
