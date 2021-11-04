package fr.bastoup.bperipherals.peripherals.database;

import dan200.computercraft.api.ComputerCraftAPI;
import fr.bastoup.bperipherals.init.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class InventoryDatabase extends ItemStackHandler {

    private final BlockEntityDatabase database;

    public InventoryDatabase(BlockEntityDatabase database) {
        super(1);
        this.database = database;
    }

    public Integer setDiskId(boolean force) {
        if (database == null)
            return null;

        ItemStack item = super.stacks.get(0);
        if (item.getItem().equals(ModItems.DATABASE_DISK)) {
            CompoundTag tag = item.getOrCreateTag();
            if (!force && tag.contains("databaseId")) {
                return null;
            }
            Level world = database.getLevel();
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
            CompoundTag tag = item.getTag();
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
            CompoundTag tag = item.getOrCreateTag();
            if (tag.contains("label")) {
                return tag.getString("label");
            }
        }
        return null;
    }

    public void setDiskName(String name) {
        ItemStack item = super.stacks.get(0);
        if (item.getItem().equals(ModItems.DATABASE_DISK)) {
            CompoundTag tag = item.getOrCreateTag();
            tag.putString("label", name);
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
