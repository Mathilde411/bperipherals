package fr.bastoup.bperipherals.init;

import dan200.computercraft.shared.computer.inventory.ContainerComputer;
import dan200.computercraft.shared.network.container.ComputerContainerData;
import dan200.computercraft.shared.network.container.ContainerData;
import fr.bastoup.bperipherals.peripherals.database.ContainerDatabase;
import fr.bastoup.bperipherals.peripherals.keyboard.KeyboardContainer;
import fr.bastoup.bperipherals.util.BPeripheralsProperties;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.extensions.IForgeContainerType;

public class ModContainerTypes {
    public static final MenuType<ContainerDatabase> DATABASE = (MenuType<ContainerDatabase>) IForgeContainerType.create(ContainerDatabase::new)
                    .setRegistryName(new ResourceLocation(BPeripheralsProperties.MODID, "database"));

    public static final MenuType<KeyboardContainer> KEYBOARD = (MenuType<KeyboardContainer>) ContainerData.toType(ComputerContainerData::new, KeyboardContainer::new)
                    .setRegistryName(new ResourceLocation(BPeripheralsProperties.MODID, "keyboard"));
}
