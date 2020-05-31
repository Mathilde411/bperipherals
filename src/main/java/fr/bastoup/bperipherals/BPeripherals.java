package fr.bastoup.bperipherals;

import fr.bastoup.bperipherals.database.DBFactory;
import fr.bastoup.bperipherals.registry.ClientRegistry;
import fr.bastoup.bperipherals.registry.SharedRegistry;
import fr.bastoup.bperipherals.util.BPeripheralsProperties;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(BPeripheralsProperties.MODID)
public class BPeripherals
{

    private static final Logger LOGGER = LogManager.getLogger();
    private static IEventBus MOD_EVENT_BUS = null;
    private static DBFactory dbFactory;

    public BPeripherals() {
        dbFactory = DBFactory.getInstance();
        MOD_EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();
        MOD_EVENT_BUS.register(SharedRegistry.class);
        MOD_EVENT_BUS.register(ClientRegistry.class);
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    public static DBFactory getDBFactory() {
        return dbFactory;
    }
}
