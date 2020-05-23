package fr.bastoup.bperipherals;

import fr.bastoup.bperipherals.init.ModBlocks;
import fr.bastoup.bperipherals.init.ModItems;
import fr.bastoup.bperipherals.init.ModTileTypes;
import fr.bastoup.bperipherals.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
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

    public BPeripherals() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onItemRegister);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onBlockRegister);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onTileRegister);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onModelRegister);

        MinecraftForge.EVENT_BUS.register(this);
    }

    public void onItemRegister(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(ModItems.ITEMS.toArray(new Item[0]));
    }

    public void onBlockRegister(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(ModBlocks.BLOCKS.toArray(new Block[0]));
    }

    public void onTileRegister(RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry().register(
                ModTileTypes.RF_METER
        );
    }

    public void onModelRegister(ModelRegistryEvent event) {
        for (Item item : ModItems.ITEMS) {
            if(item instanceof IHasModel) {
                ((IHasModel) item).registerModels();
            }
        }

        for (Block block : ModBlocks.BLOCKS) {
            if(block instanceof IHasModel) {
                ((IHasModel) block).registerModels();
            }
        }
    }

	public static Logger getLogger() {
		return LOGGER;
	}
}
