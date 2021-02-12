package fr.bastoup.bperipherals.peripherals.database;

import dan200.computercraft.api.peripheral.IComputerAccess;
import fr.bastoup.bperipherals.init.ModItems;
import fr.bastoup.bperipherals.init.ModTileTypes;
import fr.bastoup.bperipherals.util.Util;
import fr.bastoup.bperipherals.util.blocks.BlockOrientable;
import fr.bastoup.bperipherals.util.tiles.TilePeripheral;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.INameable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TileDatabase extends TilePeripheral implements INamedContainerProvider, ITickableTileEntity, INameable {
	private final InventoryDatabase databaseInventory = new InventoryDatabase(this);

	private final LazyOptional<InventoryDatabase> holderInv = LazyOptional.of(() -> databaseInventory);

	private boolean lastDiskState = false;
	private ITextComponent customName;

	public TileDatabase() {
		super(ModTileTypes.DATABASE);
		this.setPeripheral(new PeripheralDatabase(this));
	}


	public Path getDatabaseFile() throws IllegalAccessException, IOException {

		Path worldDirectory = Util.getWorldFolder((ServerWorld) world);

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
		this.markDirty();
		return id;
	}

	public String getDatabaseName() {
		String name = databaseInventory.getDiskName();
		this.markDirty();
		return name;
	}

	public void setDatabaseName(String name) {
		databaseInventory.setDiskName(name);
		this.markDirty();
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
	public void read(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
		super.read(state, nbt);
		databaseInventory.deserializeNBT(nbt.getCompound("databaseInventory"));
		setCustomName(nbt.contains("customName") ? ITextComponent.Serializer.getComponentFromJson(nbt.getString("CustomName")) : null);
	}

	@Nonnull
	@Override
	public CompoundNBT write(CompoundNBT nbt) {
		nbt.put("databaseInventory", databaseInventory.serializeNBT());
		if (this.customName != null) {
			nbt.putString("customName", ITextComponent.Serializer.toJson(this.customName));
		}
		return super.write(nbt);
	}


	@Override
	public void tick() {
		if (world == null)
			return;

		if (!world.getBlockState(this.getPos()).hasProperty(BlockDatabase.DISK_INSERTED))
			return;

		if (isDiskInserted() && !lastDiskState) {


			world.setBlockState(this.getPos(), this.getBlockState().with(BlockDatabase.DISK_INSERTED, true));

			synchronized (computers) {
				for (IComputerAccess c : computers) {
					c.queueEvent("database_attached", c.getAttachmentName(), getDatabaseId(), getDatabaseName());
				}
			}
		} else if (!isDiskInserted() && lastDiskState) {
			world.setBlockState(this.getPos(), this.getBlockState().with(BlockDatabase.DISK_INSERTED, false));

			synchronized (computers) {
				for (IComputerAccess c : computers) {
					c.queueEvent("database_detached", c.getAttachmentName());
				}
			}
		}
		lastDiskState = isDiskInserted();
	}

	@Nullable
	@Override
	public Container createMenu(int id, @Nonnull PlayerInventory inventory, @Nonnull PlayerEntity player) {
		return new ContainerDatabase(id, inventory, databaseInventory);
	}

	@Override
	public void destroy() {
		this.ejectContents(true);
	}

	private synchronized void ejectContents(boolean destroyed) {
		if (world == null)
			return;
		if (!world.isRemote && databaseInventory.isDiskInserted()) {
			ItemStack disks = databaseInventory.getStackInSlot(0);
			databaseInventory.setStackInSlot(0, ItemStack.EMPTY);
			int xOff = 0;
			int zOff = 0;
			if (!destroyed) {
				Direction dir = world.getBlockState(pos).get(BlockOrientable.FACING);
				xOff = dir.getXOffset();
				zOff = dir.getZOffset();
			}

			BlockPos pos = this.getPos();
			double x = (double) pos.getX() + 0.5D + (double) xOff * 0.5D;
			double y = (double) pos.getY() + 0.75D;
			double z = (double) pos.getZ() + 0.5D + (double) zOff * 0.5D;
			ItemEntity entityitem = new ItemEntity(world, x, y, z, disks);
			entityitem.setMotion((double) xOff * 0.15D, 0.0D, (double) zOff * 0.15D);
			world.addEntity(entityitem);

		}
	}

	@Override
	public ActionResultType onActivate(PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		if (world == null)
			return ActionResultType.PASS;

		if (player.isCrouching()) {
			ItemStack item = player.getHeldItem(hand);
			if (item.isEmpty()) {
				return ActionResultType.PASS;
			} else {
				if (!world.isRemote && !databaseInventory.isDiskInserted() && item.getItem().equals(ModItems.DATABASE_DISK)) {
					ItemStack ret = databaseInventory.insertItem(0, item, false);
					player.setHeldItem(hand, ret);
				}
				return ActionResultType.SUCCESS;
			}
		} else {
			if (!world.isRemote) {
				NetworkHooks.openGui((ServerPlayerEntity) player, this, pos);
			}

			return ActionResultType.SUCCESS;
		}
	}

	public boolean hasCustomName() {
		return this.customName != null;
	}

	@Nullable
	public ITextComponent getCustomName() {
		return this.customName;
	}

	public void setCustomName(ITextComponent customName) {
		this.customName = customName;
	}

	@Nonnull
	public ITextComponent getName() {
		return this.customName != null ? this.customName : new TranslationTextComponent(this.getBlockState().getBlock().getTranslationKey());
	}

	@Nonnull
	@Override
	public ITextComponent getDisplayName() {
		return getName();
	}
}
