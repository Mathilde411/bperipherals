package fr.bastoup.bperipherals.peripherals.database;

import dan200.computercraft.api.peripheral.IComputerAccess;
import fr.bastoup.bperipherals.init.ModItems;
import fr.bastoup.bperipherals.init.ModTileTypes;
import fr.bastoup.bperipherals.util.Util;
import fr.bastoup.bperipherals.util.blocks.BlockOrientable;
import fr.bastoup.bperipherals.util.tiles.BlockEntityPeripheral;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.Nameable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fmllegacy.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BlockEntityDatabase extends BlockEntityPeripheral implements MenuProvider, Nameable {
	private final InventoryDatabase databaseInventory = new InventoryDatabase(this);

	private final LazyOptional<InventoryDatabase> holderInv = LazyOptional.of(() -> databaseInventory);

	private boolean lastDiskState = false;
	private Component customName;

	public BlockEntityDatabase(BlockPos pos, BlockState state) {
		super(ModTileTypes.DATABASE, pos, state);
		this.setPeripheral(new PeripheralDatabase(this));
	}


	public Path getDatabaseFile() throws IllegalAccessException, IOException {

		Path worldDirectory = Util.getWorldFolder((ServerLevel) this.getLevel());

		Integer databaseId = databaseInventory.getDiskId(true);
		if (databaseId == null || databaseId == -1) {
			return null;
		}
		Path folder = worldDirectory.resolve("computercraft/database/" + databaseId);
		Path file = folder.resolve("database.db");

		try {
			Files.createDirectories(folder);
		} catch (FileAlreadyExistsException e) {
			// Do nothing
		}

		try {
			Files.createFile(file);
		} catch (FileAlreadyExistsException e) {
			//Do nothing
		}

		return file;
	}

	public Integer getDatabaseId() {
		Integer id = databaseInventory.getDiskId(true);
		this.setChanged();
		return id;
	}

	public String getDatabaseName() {
		String name = databaseInventory.getDiskName();
		this.setChanged();
		return name;
	}

	public void setDatabaseName(String name) {
		databaseInventory.setDiskName(name);
		this.setChanged();
	}

	public boolean isDiskInserted() {
		return databaseInventory.isDiskInserted();
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {
		if (capability.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)) {
			return holderInv.cast();
		}

		return super.getCapability(capability, facing);

	}


	@Override
	public void deserializeNBT(@Nonnull CompoundTag nbt) {
		super.deserializeNBT(nbt);
		databaseInventory.deserializeNBT(nbt.getCompound("databaseInventory"));
		setCustomName(nbt.contains("customName") ? Component.Serializer.fromJson(nbt.getString("CustomName")) : null);
	}

	@Nonnull
	@Override
	public CompoundTag serializeNBT() {
		CompoundTag nbt = super.serializeNBT();
		nbt.put("databaseInventory", databaseInventory.serializeNBT());
		if (this.customName != null) {
			nbt.putString("customName", Component.Serializer.toJson(this.customName));
		}
		return nbt;
	}

	@Override
	public CompoundTag save(CompoundTag nbtParam) {
		CompoundTag nbt = super.save(nbtParam);
		nbt.put("databaseInventory", databaseInventory.serializeNBT());
		if (this.customName != null) {
			nbt.putString("customName", Component.Serializer.toJson(this.customName));
		}
		return nbt;
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		databaseInventory.deserializeNBT(nbt.getCompound("databaseInventory"));
		setCustomName(nbt.contains("customName") ? Component.Serializer.fromJson(nbt.getString("CustomName")) : null);
	}



	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int id, @Nonnull Inventory inventory, @Nonnull Player player) {
		return new ContainerDatabase(id, inventory, databaseInventory);
	}

	@Override
	public void destroy() {
		this.ejectContents(true);
	}

	private synchronized void ejectContents(boolean destroyed) {
		if (this.getLevel() == null)
			return;
		if (!this.getLevel().isClientSide && databaseInventory.isDiskInserted()) {
			ItemStack disks = databaseInventory.getStackInSlot(0);
			databaseInventory.setStackInSlot(0, ItemStack.EMPTY);
			int xOff = 0;
			int zOff = 0;
			if (!destroyed) {
				Direction dir = this.getLevel().getBlockState(worldPosition).getValue(BlockOrientable.FACING);
				xOff = dir.getStepX();
				zOff = dir.getStepZ();
			}

			BlockPos pos = worldPosition;
			double x = (double) pos.getX() + 0.5D + (double) xOff * 0.5D;
			double y = (double) pos.getY() + 0.75D;
			double z = (double) pos.getZ() + 0.5D + (double) zOff * 0.5D;
			ItemEntity entityitem = new ItemEntity(this.getLevel(), x, y, z, disks);
			entityitem.push((double) xOff * 0.15D, 0.0D, (double) zOff * 0.15D);
			this.getLevel().addFreshEntity(entityitem);

		}
	}

	@Override
	public InteractionResult onActivate(Player player, InteractionHand hand, BlockHitResult hit) {
		if (this.getLevel() == null)
			return InteractionResult.PASS;

		if (player.isCrouching()) {
			ItemStack item = player.getItemInHand(hand);
			if (item.isEmpty()) {
				return InteractionResult.PASS;
			} else {
				if (!this.getLevel().isClientSide && !databaseInventory.isDiskInserted() && item.getItem().equals(ModItems.DATABASE_DISK)) {
					ItemStack ret = databaseInventory.insertItem(0, item, false);
					player.setItemInHand(hand, ret);
				}
				return InteractionResult.SUCCESS;
			}
		} else {
			if (!this.level.isClientSide) {
				NetworkHooks.openGui((ServerPlayer) player, this, worldPosition);
			}

			return InteractionResult.SUCCESS;
		}
	}

	public boolean hasCustomName() {
		return this.customName != null;
	}

	@Nullable
	public Component getCustomName() {
		return this.customName;
	}

	public void setCustomName(Component customName) {
		this.customName = customName;
	}

	@Nonnull
	public Component getName() {
		return this.customName != null ? this.customName : new TranslatableComponent(this.getBlockState().getBlock().getDescriptionId());
	}

	@Nonnull
	@Override
	public Component getDisplayName() {
		return getName();
	}

	public static <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState blockState, T tile) {

		if(!(tile instanceof BlockEntityDatabase))
			return;

		BlockEntityDatabase blockEntity = (BlockEntityDatabase) tile;

		if (blockEntity.getLevel() == null)
			return;

		if (!blockEntity.getLevel().getBlockState(blockEntity.worldPosition).hasProperty(BlockDatabase.DISK_INSERTED))
			return;

		if (blockEntity.isDiskInserted() && !blockEntity.lastDiskState) {

			blockEntity.getLevel().setBlockAndUpdate(blockEntity.worldPosition, blockEntity.getBlockState().setValue(BlockDatabase.DISK_INSERTED, true));

			synchronized (blockEntity.computers) {
				for (IComputerAccess c : blockEntity.computers) {
					c.queueEvent("database_attached", c.getAttachmentName(), blockEntity.getDatabaseId(), blockEntity.getDatabaseName());
				}
			}
		} else if (!blockEntity.isDiskInserted() && blockEntity.lastDiskState) {
			blockEntity.getLevel().setBlockAndUpdate(blockEntity.worldPosition, blockEntity.getBlockState().setValue(BlockDatabase.DISK_INSERTED, false));

			synchronized (blockEntity.computers) {
				for (IComputerAccess c : blockEntity.computers) {
					c.queueEvent("database_detached", c.getAttachmentName());
				}
			}
		}
		blockEntity.lastDiskState = blockEntity.isDiskInserted();
	}
}
