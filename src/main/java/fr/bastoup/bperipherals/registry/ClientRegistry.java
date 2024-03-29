package fr.bastoup.bperipherals.registry;

import fr.bastoup.bperipherals.BPeripherals;
import fr.bastoup.bperipherals.init.ModContainerTypes;
import fr.bastoup.bperipherals.peripherals.database.GUIDatabase;
import fr.bastoup.bperipherals.peripherals.keyboard.GUIKeyboard;
import fr.bastoup.bperipherals.peripherals.keyboard.KeyboardContainer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(
        modid = BPeripherals.MOD_ID,
        value = {Dist.CLIENT}
)
public class ClientRegistry {
    @SubscribeEvent
    public static void setupClient(FMLClientSetupEvent event) {
        MenuScreens.register(ModContainerTypes.DATABASE, GUIDatabase::new);
        MenuScreens.<KeyboardContainer, GUIKeyboard>register(ModContainerTypes.KEYBOARD, GUIKeyboard::new);
    }
}
