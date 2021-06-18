package fr.bastoup.bperipherals.util.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;

import javax.annotation.Nonnull;

public abstract class BlockPeripheral extends BlockOrientable {

    public static final BooleanProperty SWITCHED_ON = BooleanProperty.create("on");

    public BlockPeripheral(String name, Material material) {
        super(name, material);
    }

    public BlockPeripheral(Properties properties, String name) {
        super(properties, name);
    }

    @Override
    @Nonnull
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return super.getStateForPlacement(context).setValue(SWITCHED_ON, false);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(SWITCHED_ON);
        super.createBlockStateDefinition(builder);
    }
}
