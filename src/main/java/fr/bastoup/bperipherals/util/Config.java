package fr.bastoup.bperipherals.util;


import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = BPeripheralsProperties.MODID)
public class Config {

    public static int MAX_DATABASE_SIZE = 1024;
    public static int MAX_RANDOM_BYTES_SIZE = 1024;
    public static int MAX_MAG_CARD_DATA = 32;
    public static int MAX_FE_METER_TRANSFER_RATE = 32000;
    public static int FE_METER_INTERNAL_BUFFER_SIZE = 64000;

    public static void setup() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ConfigValues.serverSpecs);
    }

    public static void sync() {
        MAX_DATABASE_SIZE = ConfigValues.MAX_DATABASE_SIZE.get();
        MAX_RANDOM_BYTES_SIZE = ConfigValues.MAX_RANDOM_BYTES_SIZE.get();
        MAX_MAG_CARD_DATA = ConfigValues.MAX_MAG_CARD_DATA.get();
        MAX_FE_METER_TRANSFER_RATE = ConfigValues.MAX_FE_METER_TRANSFER_RATE.get();
        FE_METER_INTERNAL_BUFFER_SIZE = ConfigValues.FE_METER_INTERNAL_BUFFER_SIZE.get();

    }

    @SubscribeEvent
    public static void sync(ModConfigEvent.Loading event) {
        sync();
    }

    @SubscribeEvent
    public static void sync(ModConfigEvent.Reloading event) {
        // Ensure file configs are reloaded. Forge should probably do this, so worth checking in the future.
        CommentedConfig config = event.getConfig().getConfigData();
        if (config instanceof CommentedFileConfig) ((CommentedFileConfig) config).load();

        sync();
    }

    public static class ConfigValues {
        // Database
        protected static final ForgeConfigSpec.ConfigValue<Integer> MAX_DATABASE_SIZE;

        //Cryptographic Accelerator
        protected static final ForgeConfigSpec.ConfigValue<Integer> MAX_RANDOM_BYTES_SIZE;

        //Mag Card Reader
        protected static final ForgeConfigSpec.ConfigValue<Integer> MAX_MAG_CARD_DATA;

        //FE Meter
        protected static final ForgeConfigSpec.ConfigValue<Integer> MAX_FE_METER_TRANSFER_RATE;
        protected static final ForgeConfigSpec.ConfigValue<Integer> FE_METER_INTERNAL_BUFFER_SIZE;

        protected static final ForgeConfigSpec serverSpecs;

        static {
            Builder builder = new Builder();

            MAX_DATABASE_SIZE = builder
                    .comment("The maximum Database Disks size in kibibytes ( 1 KiB = 1024 Bytes ).")
                    .defineInRange("max_database_size", Config.MAX_DATABASE_SIZE, 1, Integer.MAX_VALUE);

            MAX_RANDOM_BYTES_SIZE = builder
                    .comment("The maximum length of randomBytes that can be generated.")
                    .defineInRange("max_random_bytes_size", Config.MAX_RANDOM_BYTES_SIZE, 1, Integer.MAX_VALUE);

            MAX_MAG_CARD_DATA = builder
                    .comment("The maximum size of data in a Mag Card.")
                    .defineInRange("max_mag_card_data", Config.MAX_MAG_CARD_DATA, 1, Integer.MAX_VALUE);

            MAX_FE_METER_TRANSFER_RATE = builder
                    .comment("The maximum transfer rate that can be set in an FE Meter. The addition of this and fe_meter_internal_buffer_size **MUST** be BELOW 2147483647, or else THIS WILL CORRUPT YOUR WORLD")
                    .defineInRange("max_fe_meter_transfer_rate", Config.MAX_FE_METER_TRANSFER_RATE, 1, Integer.MAX_VALUE);
            FE_METER_INTERNAL_BUFFER_SIZE = builder
                    .comment("The size of FE Meters internal energy buffer.")
                    .defineInRange("fe_meter_internal_buffer_size", Config.FE_METER_INTERNAL_BUFFER_SIZE, 1, Integer.MAX_VALUE);

            serverSpecs = builder.build();

        }
    }
}
