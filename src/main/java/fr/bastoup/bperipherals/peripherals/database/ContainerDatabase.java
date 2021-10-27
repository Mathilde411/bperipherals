package fr.bastoup.bperipherals.peripherals.database;

import fr.bastoup.bperipherals.init.ModContainerTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ContainerDatabase extends AbstractContainerMenu {

    private final InventoryDatabase inventory;

    public ContainerDatabase(int id, Inventory player, InventoryDatabase inventory) {
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

    public ContainerDatabase(int i, Inventory inventory, FriendlyByteBuf buffer) {
        this(i, inventory, new InventoryDatabase(null));
    }

    public boolean canInteractWith(@Nonnull Player player) {
        return true;
    }


    @Nonnull
    public ItemStack quickMoveStack(@Nonnull Player player, int slotIndex) {
        Slot slot = this.slots.get(slotIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack existing = slot.getItem().copy();
            ItemStack result = existing.copy();
            if (slotIndex == 0) {
                if (!this.moveItemStackTo(existing, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(existing, 0, 1, false)) {
                return ItemStack.EMPTY;
            }

            if (existing.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
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

    @Override
    public boolean stillValid(Player p_75145_1_) {
        return true;
    }
}
