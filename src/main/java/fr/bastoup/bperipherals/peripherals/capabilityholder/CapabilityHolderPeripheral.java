package fr.bastoup.bperipherals.peripherals.capabilityholder;

import dan200.computercraft.api.lua.*;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IDynamicPeripheral;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CapabilityHolderPeripheral implements IDynamicPeripheral {

    private final List<IItemHandler> inventories;
    private final List<IFluidHandler> tanks;
    private final List<IEnergyStorage> batteries;
    private final String type;
    private final String[] methods;

    public CapabilityHolderPeripheral(String type, List<IItemHandler> inventories, List<IFluidHandler> tanks, List<IEnergyStorage> batteries) {
        this.type = type;
        this.inventories = inventories;
        this.tanks = tanks;
        this.batteries = batteries;
        List<String> methods = new ArrayList<>();

        if(!inventories.isEmpty()) {
            methods.add("getInventory");
            methods.add("getInventoriesNumber");
        }

        if(!tanks.isEmpty()) {
            methods.add("getTank");
            methods.add("getTanksNumber");
        }

        if(!batteries.isEmpty()) {
            methods.add("getBattery");
            methods.add("getBatteriesNumber");
        }

        this.methods = methods.toArray(String[]::new);

    }

    @Nonnull
    @Override
    public String[] getMethodNames() {

        return methods;
    }

    @Nonnull
    @Override
    public MethodResult callMethod(@Nonnull IComputerAccess computer, @Nonnull ILuaContext context, int index, @Nonnull IArguments arguments) throws LuaException {
        switch(methods[index]) {
            case "getInventory":
                if(inventories.isEmpty())
                    throw new LuaException("No inventory in this block.");

                int invNum = arguments.getInt(0);
                if(invNum >= inventories.size() || invNum < 0)
                    throw new LuaException("You must provide a valid inventory number");

                return MethodResult.of(getInventory(invNum));
            case "getInventoriesNumber":
                return MethodResult.of(inventories.size());
            case "getTank":
                if(tanks.isEmpty())
                    throw new LuaException("No tank in this block.");

                int tankNum = arguments.getInt(0);
                if(tankNum >= tanks.size() || tankNum < 0)
                    throw new LuaException("You must provide a valid tank number");

                return MethodResult.of(getTank(tankNum));
            case "getTanksNumber":
                return MethodResult.of(tanks.size());
            case "getBattery":
                if(batteries.isEmpty())
                    throw new LuaException("No battery in this block.");

                int batteryNum = arguments.getInt(0);
                if(batteryNum >= batteries.size() || batteryNum < 0)
                    throw new LuaException("You must provide a valid battery number");

                return MethodResult.of(getBattery(batteryNum));
            case "getBatteriesNumber":
                return MethodResult.of(batteries.size());
        }
        return MethodResult.of();
    }

    private List<Map<String, Object>> getInventory(int invNum) {
        IItemHandler inventory = inventories.get(invNum);
        List<Map<String, Object>> res = new ArrayList<>();
        for(int slot = 0; slot < inventory.getSlots(); slot++) {
            ItemStack item = inventory.getStackInSlot(slot);
            int limit = inventory.getSlotLimit(slot);

            Map<String, Object> tmp = new HashMap<>();
            tmp.put("slot", slot);
            if(!item.isEmpty()) {
                tmp.put("maxAmount", Math.min(limit, item.getMaxStackSize()));
                tmp.put("item", item.getItem().getRegistryName().toString());
                tmp.put("amount", item.getCount());
                if(item.isDamageableItem() && item.isDamaged()) {
                    tmp.put("damage", item.getDamageValue());
                    tmp.put("maxDamage", item.getMaxDamage());
                }
            } else {
                tmp.put("maxAmount", limit);
            }
            res.add(tmp);
        }
        return res;
    }

    private List<Map<String, Object>> getTank(int tankNum) {
        IFluidHandler tank = tanks.get(tankNum);
        List<Map<String, Object>> res = new ArrayList<>();
        for(int slot = 0; slot < tank.getTanks(); slot++) {
            FluidStack fluid = tank.getFluidInTank(slot);
            int limit = tank.getTankCapacity(slot);

            Map<String, Object> tmp = new HashMap<>();
            tmp.put("slot", slot);
            tmp.put("maxAmount", limit);
            if(!fluid.isEmpty()) {
                tmp.put("fluid", fluid.getFluid().getRegistryName().toString());
                tmp.put("amount", fluid.getAmount());
            }
            res.add(tmp);
        }
        return res;
    }

    private Map<String, Object> getBattery(int batteryNum) {
        IEnergyStorage battery = batteries.get(batteryNum);
        Map<String, Object> res = new HashMap<>();
        res.put("maxEnergyStored", battery.getMaxEnergyStored());
        res.put("energyStored", battery.getMaxEnergyStored());
        return res;
    }

    @Nonnull
    @Override
    public String getType() {
        return type;
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return false;
    }
}
