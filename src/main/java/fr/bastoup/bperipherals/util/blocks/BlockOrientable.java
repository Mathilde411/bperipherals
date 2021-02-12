package fr.bastoup.bperipherals.util.blocks;

import fr.bastoup.bperipherals.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;

import javax.annotation.Nonnull;

public abstract class BlockOrientable extends BlockBase {

    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);

    public BlockOrientable(String name, Material material) {
        super(name, material);
    }

    public BlockOrientable(final Properties properties, String name) {
        super(properties, name);
    }

    @Override
    @Nonnull
	public BlockState getStateForPlacement(BlockItemUseContext context) {
        PlayerEntity player = context.getPlayer();
        if (player != null)
            return getDefaultState().with(FACING, Util.getOppositeFacing(player.getHorizontalFacing()));
        return getDefaultState().with(FACING, Util.getOppositeFacing(Direction.NORTH));
    }

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING);
		super.fillStateContainer(builder);
	}
}
