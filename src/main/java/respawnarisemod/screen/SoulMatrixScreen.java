package respawnarisemod.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import respawnarisemod.RespawnariseMod;

public class SoulMatrixScreen extends AbstractContainerScreen<SoulMatrixMenu> {

    public static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(RespawnariseMod.MOD_ID,
                    "textures/gui/soul_matrix/soul_matrix_gui.png");
    public SoulMatrixScreen(SoulMatrixMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f,1f,1f,1f);
        RenderSystem.setShaderTexture(0,GUI_TEXTURE);
        guiGraphics.blit(
                GUI_TEXTURE,
                (width-imageWidth)/2,(height - imageHeight)/2,
                0,0,
                imageWidth,imageHeight
        );
    }
    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderTooltip(graphics,mouseX,mouseY);
    }
}
