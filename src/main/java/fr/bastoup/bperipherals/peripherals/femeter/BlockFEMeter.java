package fr.bastoup.bperipherals.peripherals.femeter;

import fr.bastoup.bperipherals.init.ModTileTypes;
import fr.bastoup.bperipherals.util.blocks.BlockPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nullable;

public class BlockFEMeter extends BlockPeripheral {

    public BlockFEMeter() {
        super(Properties.of(Material.STONE).strength(2.0F), "fe_meter");
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModTileTypes.FE_METER.create(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == ModTileTypes.FE_METER ? BlockEntityFEMeter::tick : null;
    }
}
