package fr.bastoup.bperipherals.util.handler;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import fr.bastoup.bperipherals.tileentities.TilePeripheral;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;

public class BPeripheralsProvider implements IPeripheralProvider {
    @Nonnull
    @Override
    public LazyOptional<IPeripheral> getPeripheral(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull Direction side) {
        TileEntity t = world.getTileEntity(pos);
        if (t != null) {
            if (t instanceof TilePeripheral) {
                return LazyOptional.of(((TilePeripheral) t)::getPeripheral);
            }
        }
        return LazyOptional.empty();
    }
}
