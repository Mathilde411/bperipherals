package fr.bastoup.bperipherals.util.blocks;

import fr.bastoup.bperipherals.util.Util;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.core.Direction;

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
	public BlockState getStateForPlacement(BlockPlaceContext context) {
        Player player = context.getPlayer();
        if (player != null)
            return defaultBlockState().setValue(FACING, Util.getOppositeFacing(player.getDirection()));
        return defaultBlockState().setValue(FACING, Util.getOppositeFacing(Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        super.createBlockStateDefinition(builder);
    }
}
