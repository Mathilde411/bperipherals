package fr.bastoup.bperipherals.registry;

import fr.bastoup.bperipherals.gui.GUIDatabase;
import fr.bastoup.bperipherals.init.ModContainerTypes;
import fr.bastoup.bperipherals.util.BPeripheralsProperties;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(
        modid = BPeripheralsProperties.MODID,
        value = {Dist.CLIENT}
)
public class ClientRegistry {
    @SubscribeEvent
    public static void setupClient(FMLClientSetupEvent event) {
        ScreenManager.registerFactory(ModContainerTypes.DATABASE, GUIDatabase::new);
    }
}
