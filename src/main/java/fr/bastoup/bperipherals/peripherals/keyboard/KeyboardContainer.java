package fr.bastoup.bperipherals.peripherals.keyboard;

import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.core.IComputer;
import dan200.computercraft.shared.computer.inventory.ContainerComputerBase;
import dan200.computercraft.shared.network.container.ComputerContainerData;
import fr.bastoup.bperipherals.init.ModContainerTypes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

import java.util.function.Predicate;

public class KeyboardContainer extends ContainerComputerBase {
    public KeyboardContainer(int id, Predicate<Player> canUse, IComputer computer, ComputerFamily family) {
        super(ModContainerTypes.KEYBOARD, id, canUse, computer, family);
    }

    public KeyboardContainer(int id, Inventory player, ComputerContainerData data) {
        super(ModContainerTypes.KEYBOARD, id, player, data);
    }
}
