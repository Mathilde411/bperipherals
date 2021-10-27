package fr.bastoup.bperipherals.init;

import fr.bastoup.bperipherals.peripherals.database.ContainerDatabase;
import fr.bastoup.bperipherals.util.BPeripheralsProperties;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.extensions.IForgeContainerType;

public class ModContainerTypes {
    public static final MenuType<ContainerDatabase> DATABASE =
            (MenuType<ContainerDatabase>) IForgeContainerType.create(ContainerDatabase::new)
                    .setRegistryName(new ResourceLocation(BPeripheralsProperties.MODID, "database"));
}
