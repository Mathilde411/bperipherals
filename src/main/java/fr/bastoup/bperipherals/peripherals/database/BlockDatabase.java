package fr.bastoup.bperipherals.peripherals.database;

import fr.bastoup.bperipherals.init.ModTileTypes;
import fr.bastoup.bperipherals.util.BPeripheralsProperties;
import fr.bastoup.bperipherals.util.blocks.BlockPeripheral;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mod.EventBusSubscriber(
        modid = BPeripheralsProperties.MODID
)
public class BlockDatabase extends BlockPeripheral {

    public static final BooleanProperty DISK_INSERTED = BooleanProperty.create("disk");

    public BlockDatabase() {
        super(Properties.of(Material.STONE).strength(2.0F), "database");
    }

    @Nonnull
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(DISK_INSERTED, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DISK_INSERTED);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == ModTileTypes.DATABASE ? BlockEntityDatabase::tick : null;
    }

    public void onBlockPlacedBy(@Nonnull Level world, @Nonnull BlockPos pos, @Nonnull BlockState state, LivingEntity placer, ItemStack stack) {
        if (stack.hasCustomHoverName()) {
            BlockEntity tileentity = world.getBlockEntity(pos);
            if (tileentity instanceof BlockEntityDatabase) {
                ((BlockEntityDatabase) tileentity).setCustomName(stack.getDisplayName());
            }
        }

    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModTileTypes.DATABASE.create(pos, state);
    }
}
