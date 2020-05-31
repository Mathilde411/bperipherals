package fr.bastoup.bperipherals.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import fr.bastoup.bperipherals.containers.ContainerDatabase;
import fr.bastoup.bperipherals.util.BPeripheralsProperties;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;


public class GUIDatabase extends ContainerScreen<ContainerDatabase> {
    private static final ResourceLocation BACKGROUND = new ResourceLocation(BPeripheralsProperties.MODID, "textures/gui/database.png");

    public GUIDatabase(ContainerDatabase container, PlayerInventory player, ITextComponent title) {
        super(container, player, title);
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String title = this.title.getFormattedText();
        this.font.drawString(title, (float) (this.xSize - this.font.getStringWidth(title)) / 2.0F, 6.0F, 4210752);
        this.font.drawString(title, 8.0F, (float) (this.ySize - 96 + 2), 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(BACKGROUND);
        this.blit(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}
