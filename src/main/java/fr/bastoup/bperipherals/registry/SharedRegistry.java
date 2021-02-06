package fr.bastoup.bperipherals.registry;

import fr.bastoup.bperipherals.init.ModBlocks;
import fr.bastoup.bperipherals.init.ModContainerTypes;
import fr.bastoup.bperipherals.init.ModItems;
import fr.bastoup.bperipherals.init.ModTileTypes;
import fr.bastoup.bperipherals.util.BPeripheralsProperties;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
        modid = BPeripheralsProperties.MODID
)
public class SharedRegistry {
    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(ModItems.ITEMS.toArray(new Item[0]));
    }

    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(ModBlocks.BLOCKS.toArray(new Block[0]));
    }

    @SubscribeEvent
    public static void onTileRegister(RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry().registerAll(
                ModTileTypes.FE_METER,
                ModTileTypes.DATABASE,
                ModTileTypes.CRYPTOGRAPHIC_ACCELERATOR
        );
    }

    @SubscribeEvent
    public static void onContainerRegister(RegistryEvent.Register<ContainerType<?>> event) {
        event.getRegistry().registerAll(
                ModContainerTypes.DATABASE
        );
    }
}
