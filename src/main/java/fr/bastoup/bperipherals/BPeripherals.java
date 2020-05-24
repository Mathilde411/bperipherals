package fr.bastoup.bperipherals;

import fr.bastoup.bperipherals.init.ModBlocks;
import fr.bastoup.bperipherals.init.ModItems;
import fr.bastoup.bperipherals.init.ModTileTypes;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.bastoup.bperipherals.util.BPeripheralsProperties;
import net.minecraftforge.fml.common.Mod;

@Mod(BPeripheralsProperties.MODID)
public class BPeripherals
{

    private static final Logger LOGGER = LogManager.getLogger();
    private static IEventBus MOD_EVENT_BUS = null;

    public BPeripherals() {

        MOD_EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();
        MOD_EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onItemRegister(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(ModItems.ITEMS.toArray(new Item[0]));
    }

    @SubscribeEvent
    public void onBlockRegister(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(ModBlocks.BLOCKS.toArray(new Block[0]));
    }

    @SubscribeEvent
    public void onTileRegister(RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry().register(
                ModTileTypes.RF_METER
        );
    }

	public static Logger getLogger() {
		return LOGGER;
	}
}
