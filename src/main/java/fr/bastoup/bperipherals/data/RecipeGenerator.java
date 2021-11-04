package fr.bastoup.bperipherals.data;

import dan200.computercraft.shared.Registry;
import dan200.computercraft.shared.util.Colour;
import fr.bastoup.bperipherals.init.ModBlocks;
import fr.bastoup.bperipherals.init.ModItems;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

import static dan200.computercraft.data.Tags.CCTags.COMPUTER;

public class RecipeGenerator extends RecipeProvider {
    public RecipeGenerator(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> add) {
        basicRecipes(add);
        super.buildCraftingRecipes(add);
    }

    private void basicRecipes( @Nonnull Consumer<FinishedRecipe> add )
    {

        ShapedRecipeBuilder
                .shaped( ModBlocks.DATABASE )
                .pattern( "#R#" )
                .pattern( "#D#" )
                .pattern( "#I#" )
                .define( '#', Tags.Items.STONE )
                .define( 'R', Tags.Items.DUSTS_REDSTONE )
                .define( 'D', Registry.ModItems.DISK_DRIVE.get() )
                .define( 'I', Tags.Items.INGOTS_IRON )
                .unlockedBy( "has_computer", inventoryChange(COMPUTER) )
                .unlockedBy( "has_disk_drive", inventoryChange(Registry.ModItems.DISK_DRIVE.get()) )
                .save( add );

        ShapedRecipeBuilder
                .shaped( ModBlocks.CRYPTOGRAPHIC_ACCELERATOR )
                .pattern( "#G#" )
                .pattern( "#R#" )
                .pattern( "#G#" )
                .define( '#', Tags.Items.STONE )
                .define( 'R', Tags.Items.DUSTS_REDSTONE )
                .define( 'G', Tags.Items.INGOTS_GOLD )
                .unlockedBy( "has_computer", inventoryChange(COMPUTER) )
                .save( add );

        ShapedRecipeBuilder
                .shaped( ModBlocks.MAG_CARD_READER )
                .pattern( "###" )
                .pattern( "#R#" )
                .pattern( "#I#" )
                .define( '#', Tags.Items.STONE )
                .define( 'R', Tags.Items.DUSTS_REDSTONE )
                .define( 'I', Tags.Items.INGOTS_IRON )
                .unlockedBy( "has_computer", inventoryChange(COMPUTER) )
                .save( add );

        ShapelessRecipeBuilder
                .shapeless(ModItems.MAG_CARD)
                .requires(Tags.Items.INGOTS_IRON)
                .requires(Tags.Items.DUSTS_REDSTONE)
                .requires(Items.PAPER)
                .unlockedBy("has_mag_card_reader", inventoryChange(ModBlocks.MAG_CARD_READER.asItem()))
                .save(add);

        ShapedRecipeBuilder
                .shaped( ModItems.DATABASE_DISK )
                .pattern( " R " )
                .pattern( "IDI" )
                .pattern( " I " )
                .define( 'D', Registry.ModItems.DISK.get() )
                .define( 'R',  Tags.Items.DUSTS_REDSTONE )
                .define( 'I', Tags.Items.INGOTS_IRON )
                .unlockedBy( "has_database", inventoryChange(ModBlocks.DATABASE.asItem()))
                .save( add );

        ShapedRecipeBuilder
                .shaped( ModItems.KEYBOARD )
                .pattern( "BBB" )
                .pattern( "BRB" )
                .pattern( "###" )
                .define( '#', Tags.Items.STONE )
                .define( 'R', Tags.Items.DUSTS_REDSTONE )
                .define( 'B', Items.STONE_BUTTON )
                .unlockedBy( "has_computer", inventoryChange(COMPUTER) )
                .save( add );
    }

    private static InventoryChangeTrigger.TriggerInstance inventoryChange( ItemLike... stack )
    {
        return InventoryChangeTrigger.TriggerInstance.hasItems( stack );
    }

    private static InventoryChangeTrigger.TriggerInstance inventoryChange(Tag<Item> stack )
    {
        return InventoryChangeTrigger.TriggerInstance.hasItems( ItemPredicate.Builder.item().of( stack ).build() );
    }
}
