package fr.bastoup.bperipherals.peripherals.database;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import fr.bastoup.bperipherals.util.BPeripheralsProperties;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;


public class GUIDatabase extends ContainerScreen<ContainerDatabase> {
    private static final ResourceLocation BACKGROUND = new ResourceLocation(BPeripheralsProperties.MODID, "textures/gui/database.png");

    public GUIDatabase( ContainerDatabase container, PlayerInventory player, ITextComponent title )
    {
        super( container, player, title );
    }

    @Override
    public void render( @Nonnull MatrixStack transform, int mouseX, int mouseY, float partialTicks )
    {
        renderBackground( transform );
        super.render(transform, mouseX, mouseY, partialTicks);
        renderTooltip(transform, mouseX, mouseY);
    }


    @Override
    @SuppressWarnings("deprecation")
    protected void renderBg(@Nonnull MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.getTextureManager().bind(BACKGROUND);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);

    }
}
