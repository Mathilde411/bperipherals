package fr.bastoup.bperipherals.peripherals.keyboard;

import dan200.computercraft.shared.computer.blocks.TileComputer;
import fr.bastoup.bperipherals.items.ItemKeyboard;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

import javax.annotation.Nullable;

public class KeyboardMenuProvider implements MenuProvider {

    private TileComputer computer;

    public KeyboardMenuProvider(TileComputer computer) {
        this.computer = computer;
    }

    @Override
    public Component getDisplayName() {
        return computer.getDisplayName();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new KeyboardContainer(id, pl -> ItemKeyboard.canUseComputer(pl, computer), computer.createServerComputer(), computer.getFamily());
    }
}
