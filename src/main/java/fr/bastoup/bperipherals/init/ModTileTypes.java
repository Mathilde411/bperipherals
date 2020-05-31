package fr.bastoup.bperipherals.init;

import fr.bastoup.bperipherals.tileentities.TileDatabase;
import fr.bastoup.bperipherals.tileentities.TileFEMeter;
import fr.bastoup.bperipherals.util.BPeripheralsProperties;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;

public class ModTileTypes {

    public static final TileEntityType<TileFEMeter> FE_METER = (TileEntityType<TileFEMeter>) TileEntityType.Builder.create(TileFEMeter::new, ModBlocks.FE_METER)
            .build(null).setRegistryName(new ResourceLocation(BPeripheralsProperties.MODID, "fe_meter"));
    public static final TileEntityType<TileFEMeter> DATABASE = (TileEntityType<TileFEMeter>) TileEntityType.Builder.create(TileDatabase::new, ModBlocks.DATABASE)
            .build(null).setRegistryName(new ResourceLocation(BPeripheralsProperties.MODID, "database"));
}
