package fr.bastoup.bperipherals.peripherals.database;

import fr.bastoup.bperipherals.init.ModTileTypes;
import fr.bastoup.bperipherals.util.BPeripheralsProperties;
import fr.bastoup.bperipherals.util.blocks.BlockPeripheral;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.INameable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mod.EventBusSubscriber(
        modid = BPeripheralsProperties.MODID
)
public class BlockDatabase extends BlockPeripheral {

    public static final BooleanProperty DISK_INSERTED = BooleanProperty.create("disk");

    public BlockDatabase() {
        super(Properties.create(Material.ROCK).hardnessAndResistance(2.0F), "database");
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModTileTypes.DATABASE.create();
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return super.getStateForPlacement(context).with(DISK_INSERTED, false);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(DISK_INSERTED);
        super.fillStateContainer(builder);
    }

    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (stack.hasDisplayName()) {
            TileEntity tileentity = world.getTileEntity(pos);
            if (tileentity instanceof TileDatabase) {
                ((TileDatabase) tileentity).setCustomName(stack.getDisplayName());
            }
        }

    }

    @Override
    public void harvestBlock(@Nonnull World world, PlayerEntity player, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nullable TileEntity te, ItemStack stack) {
        if (te instanceof INameable && ((INameable) te).hasCustomName()) {
            player.addStat(Stats.BLOCK_MINED.get(this));
            player.addExhaustion(0.005F);
            ItemStack result = new ItemStack(this);
            result.setDisplayName(((INameable) te).getCustomName());
            spawnAsEntity(world, pos, result);
        } else {
            super.harvestBlock(world, player, pos, state, te, stack);
        }

    }
}
