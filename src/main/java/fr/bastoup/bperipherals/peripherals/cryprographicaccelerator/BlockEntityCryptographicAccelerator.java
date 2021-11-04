package fr.bastoup.bperipherals.peripherals.cryprographicaccelerator;

import fr.bastoup.bperipherals.init.ModTileTypes;
import fr.bastoup.bperipherals.util.tiles.BlockEntityPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityCryptographicAccelerator extends BlockEntityPeripheral {
    public BlockEntityCryptographicAccelerator(BlockPos pos, BlockState state) {
        super(ModTileTypes.CRYPTOGRAPHIC_ACCELERATOR, pos, state);
        this.setPeripheral(new PeripheralCryptographicAccelerator(this));
    }
}
