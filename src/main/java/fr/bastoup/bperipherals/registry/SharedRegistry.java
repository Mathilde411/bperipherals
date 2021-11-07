package fr.bastoup.bperipherals.registry;

import fr.bastoup.bperipherals.BPeripherals;
import fr.bastoup.bperipherals.init.ModBlocks;
import fr.bastoup.bperipherals.init.ModContainerTypes;
import fr.bastoup.bperipherals.init.ModItems;
import fr.bastoup.bperipherals.init.ModTileTypes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
        modid = BPeripherals.MOD_ID
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
    public static void onTileRegister(RegistryEvent.Register<BlockEntityType<?>> event) {
        event.getRegistry().registerAll(
                ModTileTypes.FE_METER,
                ModTileTypes.DATABASE,
                ModTileTypes.CRYPTOGRAPHIC_ACCELERATOR,
                ModTileTypes.MAG_CARD_READER
        );
    }

    @SubscribeEvent
    public static void onContainerRegister(RegistryEvent.Register<MenuType<?>> event) {
        event.getRegistry().registerAll(
                ModContainerTypes.DATABASE,
                ModContainerTypes.KEYBOARD
        );
    }
}
