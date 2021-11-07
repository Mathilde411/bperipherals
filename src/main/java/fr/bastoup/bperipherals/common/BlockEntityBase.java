package fr.bastoup.bperipherals.common;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public abstract class BlockEntityBase extends BlockEntity {

    public BlockEntityBase(BlockEntityType<? extends BlockEntityBase> tileType, BlockPos pos, BlockState state) {
        super(tileType, pos, state);
    }

    public void destroy() {

    }

    public InteractionResult onActivate(Player player, InteractionHand hand, BlockHitResult hit) {
        return InteractionResult.PASS;
    }
}
