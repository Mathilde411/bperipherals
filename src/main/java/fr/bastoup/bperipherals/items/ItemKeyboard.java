package fr.bastoup.bperipherals.items;

import dan200.computercraft.shared.computer.blocks.BlockComputer;
import dan200.computercraft.shared.computer.blocks.TileComputer;
import dan200.computercraft.shared.network.container.ComputerContainerData;
import fr.bastoup.bperipherals.peripherals.keyboard.KeyboardMenuProvider;
import fr.bastoup.bperipherals.util.Config;
import fr.bastoup.bperipherals.common.ItemBase;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;

public class ItemKeyboard extends ItemBase {

    public ItemKeyboard() {
        super("keyboard", 1);
    }

    public static boolean canUseComputer(Player player, TileComputer computer) {
        if( !computer.isUsable(player, true) )
            return false;

        double range = Config.MAX_KEYBOARD_INTERACT_DISTANCE;
        BlockPos pos = computer.getBlockPos();
        return player.getCommandSenderWorld() == computer.getLevel() &&
                player.distanceToSqr( pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5 ) <= range * range;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if(player.isCrouching()) {
            InteractionResult res = unbindComputer(level, player, stack);
            if(res == null)
                return super.use(level, player, hand);
        }

        InteractionResult res = keyboardUse(level, player, stack);
        if(res == null)
            return super.use(level, player, hand);
        return new InteractionResultHolder<>(res, stack);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {

        Level level = context.getLevel();
        if(level.isClientSide)
            return InteractionResult.SUCCESS;

        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();
        CompoundTag tag = stack.getOrCreateTag();
        if(player.isCrouching()) {
            BlockState state = level.getBlockState(pos);
            if(state.getBlock() instanceof BlockComputer) {
                tag.put("computerPos", NbtUtils.writeBlockPos(pos));
                player.displayClientMessage(new TranslatableComponent("item.bperipherals.keyboard.bound"), true);
            } else {
                InteractionResult res = unbindComputer(level, player, stack);
                if(res != null)
                    return res;
            }
        } else {
            InteractionResult res = keyboardUse(level, player, stack);
            if(res != null)
                return res;
        }

        return InteractionResult.PASS;
    }

    public InteractionResult keyboardUse(Level level, Player player, ItemStack stack) {
        if(level.isClientSide || player.isCrouching() || !(stack.getItem() instanceof ItemKeyboard))
            return null;

        CompoundTag tag = stack.getOrCreateTag();
        if(tag.contains("computerPos")) {
            BlockPos computerPos = NbtUtils.readBlockPos(tag.getCompound("computerPos"));
            BlockState state = level.getBlockState(computerPos);
            if(state.getBlock() instanceof BlockComputer) {
                TileComputer tile = (TileComputer) level.getBlockEntity(computerPos);
                if(canUseComputer(player, tile)) {
                    tile.createServerComputer().turnOn();
                    new ComputerContainerData( tile.createServerComputer() ).open( player, new KeyboardMenuProvider(tile));
                    player.displayClientMessage(new TranslatableComponent("item.bperipherals.keyboard.connected"), true);
                } else {
                    player.displayClientMessage(new TranslatableComponent("item.bperipherals.keyboard.toofar"), true);
                    return InteractionResult.FAIL;
                }
            } else {
                player.displayClientMessage(new TranslatableComponent("item.bperipherals.keyboard.broken"), true);
            }
            return InteractionResult.SUCCESS;
        }
        return null;
    }

    public InteractionResult unbindComputer(Level level, Player player, ItemStack stack) {
        if(level.isClientSide || !player.isCrouching() || !(stack.getItem() instanceof ItemKeyboard))
            return null;

        CompoundTag tag = stack.getOrCreateTag();
        if(tag.contains("computerPos")) {
            tag.remove("computerPos");
            player.displayClientMessage(new TranslatableComponent("item.bperipherals.keyboard.cleared"), true);
            return InteractionResult.SUCCESS;
        }
        return null;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltips, TooltipFlag flags) {
        super.appendHoverText(stack, level, tooltips, flags);
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains("computerPos")) {
            BlockPos computerPos = NbtUtils.readBlockPos(tag.getCompound("computerPos"));

            tooltips.add(
                    new TranslatableComponent(
                            "item.bperipherals.keyboard.label",
                            computerPos.getX(),
                            computerPos.getY(),
                            computerPos.getZ())
                            .setStyle(Style.EMPTY.withColor(0xAAAAAA))
            );
        }
    }
}
