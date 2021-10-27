package fr.bastoup.bperipherals.peripherals.magcardreader;

import fr.bastoup.bperipherals.init.ModTileTypes;
import fr.bastoup.bperipherals.util.blocks.BlockPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockMagCardReader extends BlockPeripheral {

    public static final EnumProperty<BlockStateMagCardReader> STATE = EnumProperty.create("state", BlockStateMagCardReader.class);

    public BlockMagCardReader() {
        super(Properties.of(Material.STONE).strength(2.0F), "mag_card_reader");

        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(STATE, BlockStateMagCardReader.READ));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(STATE, BlockStateMagCardReader.READ);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STATE);
        super.createBlockStateDefinition(builder);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModTileTypes.MAG_CARD_READER.create(pos, state);
    }
}
