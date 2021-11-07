package fr.bastoup.bperipherals.peripherals;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import fr.bastoup.bperipherals.peripherals.capabilityholder.CapabilityHolderPeripheral;
import fr.bastoup.bperipherals.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class CapabilityPeripheralProvider implements IPeripheralProvider {
    @Nonnull
    @Override
    public LazyOptional<IPeripheral> getPeripheral(@Nonnull Level world, @Nonnull BlockPos pos, @Nonnull Direction side) {
        BlockEntity tile = world.getBlockEntity(pos);
        if(tile != null) {
            List<IItemHandler> inventories = Util.getAllCapabilities(tile, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
            List<IFluidHandler> tanks = Util.getAllCapabilities(tile, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
            List<IEnergyStorage> batteries = Util.getAllCapabilities(tile, CapabilityEnergy.ENERGY);
            return LazyOptional.of(() -> new CapabilityHolderPeripheral(
                    tile.getBlockState().getBlock().getRegistryName().toString(),
                    inventories,
                    tanks,
                    batteries
            ));
        }
        return LazyOptional.empty();
    }
}
