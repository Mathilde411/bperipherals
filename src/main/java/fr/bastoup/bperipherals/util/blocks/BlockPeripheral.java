package fr.bastoup.bperipherals.util.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;

public abstract class BlockPeripheral extends BlockOrientable {

    public static final BooleanProperty SWITCHED_ON = BooleanProperty.create("on");

    public BlockPeripheral(String name, Material material) {
        super(name, material);
    }

    public BlockPeripheral(Properties properties, String name) {
        super(properties, name);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return super.getStateForPlacement(context).with(SWITCHED_ON, false);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(SWITCHED_ON);
        super.fillStateContainer(builder);
    }
}
