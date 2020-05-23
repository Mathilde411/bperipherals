package fr.bastoup.bperipherals.init;

import fr.bastoup.bperipherals.tileentities.TileRFMeter;
import fr.bastoup.bperipherals.util.BPeripheralsProperties;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class ModTileTypes {

    public static final TileEntityType<TileRFMeter> RF_METER = (TileEntityType<TileRFMeter>) TileEntityType.Builder.create(TileRFMeter::new, ModBlocks.RF_METER)
            .build(null).setRegistryName(new ResourceLocation(BPeripheralsProperties.MODID, "rf_meter_tile"));
}
