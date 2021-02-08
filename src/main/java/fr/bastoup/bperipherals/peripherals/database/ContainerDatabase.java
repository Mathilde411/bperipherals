package fr.bastoup.bperipherals.peripherals.database;

import fr.bastoup.bperipherals.init.ModContainerTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ContainerDatabase extends Container {

    private final InventoryDatabase inventory;

    public ContainerDatabase(int id, PlayerInventory player, InventoryDatabase inventory) {
        super(ModContainerTypes.DATABASE, id);
        this.inventory = inventory;
        this.addSlot(new SlotItemHandler(inventory, 0, 80, 35));

        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlot(new Slot(player, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
            }
        }

        for (int x = 0; x < 9; ++x) {
            this.addSlot(new Slot(player, x, 8 + x * 18, 142));
        }

    }

    public ContainerDatabase(int i, PlayerInventory inventory, PacketBuffer buffer) {
        this(i, inventory, new InventoryDatabase(null));
    }

    public boolean canInteractWith(@Nonnull PlayerEntity player) {
        return true;
    }

    @Nonnull
    public ItemStack transferStackInSlot(PlayerEntity player, int slotIndex) {
        Slot slot = this.inventorySlots.get(slotIndex);
        if (slot != null && slot.getHasStack()) {
            ItemStack existing = slot.getStack().copy();
            ItemStack result = existing.copy();
            if (slotIndex == 0) {
                if (!this.mergeItemStack(existing, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(existing, 0, 1, false)) {
                return ItemStack.EMPTY;
            }

            if (existing.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (existing.getCount() == result.getCount()) {
                return ItemStack.EMPTY;
            } else {
                slot.onTake(player, existing);
                return result;
            }
        } else {
            return ItemStack.EMPTY;
        }
    }
}
