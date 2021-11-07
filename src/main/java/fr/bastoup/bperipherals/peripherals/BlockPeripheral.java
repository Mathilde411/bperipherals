package fr.bastoup.bperipherals.peripherals;

import fr.bastoup.bperipherals.common.BlockOrientable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.StateDefinition;

import javax.annotation.Nonnull;

public abstract class BlockPeripheral extends BlockOrientable implements EntityBlock {

    public static final BooleanProperty SWITCHED_ON = BooleanProperty.create("on");

    public BlockPeripheral(String name, Material material) {
        super(name, material);
    }

    public BlockPeripheral(Properties properties, String name) {
        super(properties, name);
    }

    @Override
    @Nonnull
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(SWITCHED_ON, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SWITCHED_ON);
        super.createBlockStateDefinition(builder);
    }
}
