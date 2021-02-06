package fr.bastoup.bperipherals.tileentities;

import dan200.computercraft.shared.Capabilities;
import fr.bastoup.bperipherals.init.ModTileTypes;
import fr.bastoup.bperipherals.peripherals.PeripheralCryptographicAccelerator;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class TileCryptographicAccelerator extends TileOrientable {
    public TileCryptographicAccelerator() {
        super(ModTileTypes.CRYPTOGRAPHIC_ACCELERATOR);
    }

    private final PeripheralCryptographicAccelerator peripheral = new PeripheralCryptographicAccelerator(this);

    private final LazyOptional<PeripheralCryptographicAccelerator> holderPeripheral = LazyOptional.of(() -> peripheral);

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {
        if (capability.equals(Capabilities.CAPABILITY_PERIPHERAL)) {
            return holderPeripheral.cast();
        }
        return super.getCapability(capability, facing);
    }
}
