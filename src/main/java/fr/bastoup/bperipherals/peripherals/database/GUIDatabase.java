package fr.bastoup.bperipherals.peripherals.database;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import fr.bastoup.bperipherals.BPeripherals;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

import javax.annotation.Nonnull;


public class GUIDatabase extends AbstractContainerScreen<ContainerDatabase> {
    private static final ResourceLocation BACKGROUND = new ResourceLocation(BPeripherals.MOD_ID, "textures/gui/database.png");

    public GUIDatabase( ContainerDatabase container, Inventory player, Component title )
    {
        super( container, player, title );
    }

    @Override
    public void render( @Nonnull PoseStack transform, int mouseX, int mouseY, float partialTicks )
    {
        renderBackground( transform );
        super.render( transform, mouseX, mouseY, partialTicks );
        renderTooltip( transform, mouseX, mouseY );
    }


    @Override
    @SuppressWarnings("deprecation")
    protected void renderBg(@Nonnull PoseStack transform, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderColor( 1.0F, 1.0F, 1.0F, 1.0F );
        RenderSystem.setShaderTexture( 0, BACKGROUND );
        blit( transform, leftPos, topPos, 0, 0, imageWidth, imageHeight );
    }
}
