package fr.bastoup.bperipherals.init;

import fr.bastoup.bperipherals.peripherals.database.ContainerDatabase;
import fr.bastoup.bperipherals.util.BPeripheralsProperties;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.extensions.IForgeContainerType;

public class ModContainerTypes {
    public static final ContainerType<ContainerDatabase> DATABASE =
            (ContainerType<ContainerDatabase>) IForgeContainerType.create(ContainerDatabase::new)
                    .setRegistryName(new ResourceLocation(BPeripheralsProperties.MODID, "database"));
}
