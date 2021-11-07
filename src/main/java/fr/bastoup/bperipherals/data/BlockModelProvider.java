package fr.bastoup.bperipherals.data;

import fr.bastoup.bperipherals.BPeripherals;
import fr.bastoup.bperipherals.init.ModBlocks;
import fr.bastoup.bperipherals.peripherals.database.BlockDatabase;
import fr.bastoup.bperipherals.peripherals.magcardreader.BlockMagCardReader;
import fr.bastoup.bperipherals.peripherals.magcardreader.BlockStateMagCardReader;
import fr.bastoup.bperipherals.peripherals.BlockPeripheral;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.util.Objects;

public class BlockModelProvider extends BlockStateProvider {

    public BlockModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper )
    {
        super( generator, BPeripherals.MOD_ID, existingFileHelper );
    }

    @Override
    protected void registerStatesAndModels() {
        registerPeripheral(ModBlocks.CRYPTOGRAPHIC_ACCELERATOR);
        registerPeripheral(ModBlocks.FE_METER);
        registerDatabase();
        registerMagCardReader();
    }

    private void registerDatabase() {
        VariantBlockStateBuilder builder = getVariantBuilder( ModBlocks.DATABASE );

        for(boolean on : new boolean[] {true, false}) {
            for(boolean inserted : new boolean[] {true, false}) {
                BlockModelBuilder model = models().orientableWithBottom(
                        extendedName(ModBlocks.DATABASE, (on ? "on" : "off") + "_" + (inserted ? "inserted" : "empty") ),
                        blockTexture(ModBlocks.DATABASE, "side"),
                        blockTexture(ModBlocks.DATABASE, "front_" + (on ? "on" : "off") + "_" + (inserted ? "inserted" : "empty")),
                        genericBlockTexture(ModBlocks.DATABASE.getRegistryName().getNamespace(), "machine_bottom"),
                        genericBlockTexture(ModBlocks.DATABASE.getRegistryName().getNamespace(), "machine_top")
                );

                for( Direction facing : BlockDatabase.FACING.getPossibleValues())
                {
                    builder.partialState()
                            .with( BlockDatabase.SWITCHED_ON, on )
                            .with( BlockDatabase.DISK_INSERTED, inserted )
                            .with( BlockDatabase.FACING, facing)
                            .addModels( new ConfiguredModel( model, 0, toYAngle( facing ), false ) );
                }
            }
        }
        simpleBlockItem(ModBlocks.DATABASE, models().getBuilder( extendedName( ModBlocks.DATABASE, "on_empty" ) ));
    }

    private void registerMagCardReader() {
        VariantBlockStateBuilder builder = getVariantBuilder( ModBlocks.MAG_CARD_READER );
        for(BlockStateMagCardReader state : BlockMagCardReader.STATE.getPossibleValues()) {
            BlockModelBuilder model = models().orientableWithBottom(
                    extendedName(ModBlocks.MAG_CARD_READER, state.getSerializedName() ),
                    genericBlockTexture(ModBlocks.MAG_CARD_READER.getRegistryName().getNamespace(), "machine_side"),
                    blockTexture(ModBlocks.MAG_CARD_READER, "front_" + state.getSerializedName()),
                    genericBlockTexture(ModBlocks.MAG_CARD_READER.getRegistryName().getNamespace(), "machine_bottom"),
                    genericBlockTexture(ModBlocks.MAG_CARD_READER.getRegistryName().getNamespace(), "machine_top")
            );

            for( Direction facing : BlockMagCardReader.FACING.getPossibleValues()) {
                builder.partialState()
                        .with( BlockMagCardReader.SWITCHED_ON, true)
                        .with( BlockMagCardReader.STATE, state)
                        .with( BlockMagCardReader.FACING, facing)
                        .addModels( new ConfiguredModel( model, 0, toYAngle( facing ), false ) );
            }
        }

        BlockModelBuilder model = models().orientableWithBottom(
                extendedName(ModBlocks.MAG_CARD_READER, "off" ),
                genericBlockTexture(ModBlocks.MAG_CARD_READER.getRegistryName().getNamespace(), "machine_side"),
                blockTexture(ModBlocks.MAG_CARD_READER, "front_off"),
                genericBlockTexture(ModBlocks.MAG_CARD_READER.getRegistryName().getNamespace(), "machine_bottom"),
                genericBlockTexture(ModBlocks.MAG_CARD_READER.getRegistryName().getNamespace(), "machine_top")
        );

        for(BlockStateMagCardReader state : BlockMagCardReader.STATE.getPossibleValues()) {
            for( Direction facing : BlockMagCardReader.FACING.getPossibleValues()) {
                builder.partialState()
                        .with( BlockMagCardReader.SWITCHED_ON, false)
                        .with( BlockMagCardReader.STATE, state)
                        .with( BlockMagCardReader.FACING, facing)
                        .addModels( new ConfiguredModel( model, 0, toYAngle( facing ), false ) );
            }
        }
        simpleBlockItem(ModBlocks.MAG_CARD_READER, models().getBuilder( extendedName( ModBlocks.MAG_CARD_READER, "read" ) ));
    }

    private <T extends BlockPeripheral> void registerPeripheral(T peripheral) {
        VariantBlockStateBuilder builder = getVariantBuilder( peripheral );
        for(boolean on : new boolean[] {true, false}) {
            BlockModelBuilder model = models().orientableWithBottom(
                    extendedName(peripheral, on ? "on" : "off" ),
                    genericBlockTexture(peripheral.getRegistryName().getNamespace(), "machine_side"),
                    blockTexture(peripheral, "front_" + (on ? "on" : "off")),
                    genericBlockTexture(peripheral.getRegistryName().getNamespace(), "machine_bottom"),
                    genericBlockTexture(peripheral.getRegistryName().getNamespace(), "machine_top")
            );

            for( Direction facing : BlockPeripheral.FACING.getPossibleValues()) {
                builder.partialState()
                        .with( BlockPeripheral.SWITCHED_ON, on )
                        .with( BlockPeripheral.FACING, facing )
                        .addModels( new ConfiguredModel( model, 0, toYAngle( facing ), false ) );
            }
        }
        simpleBlockItem(peripheral, models().getBuilder( extendedName( peripheral, "on" ) ));
    }

    private static ResourceLocation blockTexture(Block block, String name )
    {
        ResourceLocation id = block.getRegistryName();
        return new ResourceLocation( id.getNamespace(), "block/" + id.getPath() + "/" + id.getPath() + "_" + name );
    }

    private static ResourceLocation genericBlockTexture(String namespace, String name )
    {
        return new ResourceLocation(namespace, "block/generic/" + name );
    }

    @Nonnull
    private String name( @Nonnull IForgeRegistryEntry<?> term )
    {
        return Objects.requireNonNull( term.getRegistryName() ).toString();
    }

    @Nonnull
    private String extendedName( @Nonnull IForgeRegistryEntry<?> term, @Nonnull String name )
    {
        return extend( Objects.requireNonNull( term.getRegistryName() ), name );
    }

    @Nonnull
    private String extendedNameWithSubdirectory( @Nonnull IForgeRegistryEntry<?> term, @Nonnull String name )
    {
        return extend( Objects.requireNonNull( term.getRegistryName() ), term.getRegistryName().getPath() + "/" + name );
    }

    @Nonnull
    private String extend( @Nonnull ResourceLocation location, @Nonnull String name )
    {
        return new ResourceLocation( location.getNamespace(), location.getPath() + "_" + name ).toString();
    }

    private static int toXAngle( Direction direction )
    {
        switch( direction )
        {
            default:
                return 0;
            case UP:
                return 270;
            case DOWN:
                return 90;
        }
    }

    private static int toYAngle( Direction direction )
    {
        return ((int) direction.toYRot() + 180) % 360;
    }

}
