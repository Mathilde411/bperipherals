package fr.bastoup.bperipherals;

import dan200.computercraft.api.ComputerCraftAPI;
import fr.bastoup.bperipherals.database.DBFactory;
import fr.bastoup.bperipherals.peripherals.CapabilityPeripheralProvider;
import fr.bastoup.bperipherals.registry.ClientRegistry;
import fr.bastoup.bperipherals.registry.SharedRegistry;
import fr.bastoup.bperipherals.util.Config;
import fr.bastoup.bperipherals.common.BPeripheralsCreativeTab;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(BPeripherals.MOD_ID)
public class BPeripherals {
    public static final String MOD_ID = "bperipherals";
    public static final BPeripheralsCreativeTab CREATIVE_TAB = new BPeripheralsCreativeTab();
    public static final Logger LOGGER = LogManager.getLogger();
    public static final DBFactory DB_FACTORY = DBFactory.getInstance();

    private IEventBus modEventBus;

    public BPeripherals() {
        modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.register(SharedRegistry.class);
        modEventBus.register(ClientRegistry.class);
        modEventBus.register(Config.class);

        Config.setup();
        ComputerCraftAPI.registerPeripheralProvider(new CapabilityPeripheralProvider());

    }
}
