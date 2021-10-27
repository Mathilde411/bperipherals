package fr.bastoup.bperipherals.peripherals.cryprographicaccelerator;

import fr.bastoup.bperipherals.init.ModTileTypes;
import fr.bastoup.bperipherals.util.tiles.TilePeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileCryptographicAccelerator extends TilePeripheral {
    public TileCryptographicAccelerator(BlockPos pos, BlockState state) {
        super(ModTileTypes.CRYPTOGRAPHIC_ACCELERATOR, pos, state);
        this.setPeripheral(new PeripheralCryptographicAccelerator(this));
    }
}
