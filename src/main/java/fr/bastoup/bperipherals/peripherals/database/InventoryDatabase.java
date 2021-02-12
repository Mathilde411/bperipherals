package fr.bastoup.bperipherals.peripherals.database;

import dan200.computercraft.api.ComputerCraftAPI;
import fr.bastoup.bperipherals.init.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class InventoryDatabase extends ItemStackHandler {

    private final TileDatabase database;

    public InventoryDatabase(TileDatabase database) {
        super(1);
        this.database = database;
    }

    public Integer setDiskId(boolean force) {
        if (database == null)
            return null;

        ItemStack item = super.stacks.get(0);
        if (item.getItem().equals(ModItems.DATABASE_DISK)) {
            CompoundNBT tag = item.getOrCreateTag();
            if (!force && tag.contains("databaseId")) {
                return null;
            }
            World world = database.getWorld();
            int id = ComputerCraftAPI.createUniqueNumberedSaveDir(world, "database");
            if (id >= 0)
                tag.putInt("databaseId", id);
            return id;
        } else {
            return null;
        }
    }

    public Integer getDiskId(boolean force) {
        ItemStack item = super.stacks.get(0);
        if (item.getItem().equals(ModItems.DATABASE_DISK)) {
            CompoundNBT tag = item.getTag();
            if (tag != null && tag.contains("databaseId")) {
                return tag.getInt("databaseId");
            } else {
                if (force) {
                    return setDiskId(true);
                } else {
                    return -1;
                }
            }
        } else {
            return null;
        }
    }

    public String getDiskName() {
        ItemStack item = super.stacks.get(0);
        if (item.getItem().equals(ModItems.DATABASE_DISK)) {
            CompoundNBT tag = item.getOrCreateTag();
            if (tag.contains("databaseName")) {
                return tag.getString("databaseName");
            }
        }
        return null;
    }

    public void setDiskName(String name) {
        ItemStack item = super.stacks.get(0);
        if (item.getItem().equals(ModItems.DATABASE_DISK)) {
            CompoundNBT tag = item.getOrCreateTag();
            tag.putString("databaseName", name);
        }
    }

    public boolean isDiskInserted() {
        ItemStack item = super.stacks.get(0);
        return item.getItem().equals(ModItems.DATABASE_DISK);
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return stack.getItem().equals(ModItems.DATABASE_DISK);
    }
}
