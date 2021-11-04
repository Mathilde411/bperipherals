package fr.bastoup.bperipherals.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.MOD )
public class Generators
{
    @SubscribeEvent
    public static void gather( GatherDataEvent event )
    {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFiles = event.getExistingFileHelper();
        generator.addProvider( new BlockModelProvider( generator, existingFiles ) );
        generator.addProvider( new RecipeGenerator( generator ) );
    }
}
