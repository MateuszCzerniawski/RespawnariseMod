package respawnarisemod.util;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import respawnarisemod.RespawnariseMod;

import java.util.function.Consumer;

public class ToggleWidget extends AbstractWidget {

    private static final ResourceLocation TEXTURE_ON =
            ResourceLocation.fromNamespaceAndPath(RespawnariseMod.MOD_ID,
                    "textures/gui/toggle/toggle_on.png");
    private static final ResourceLocation TEXTURE_OFF =
            ResourceLocation.fromNamespaceAndPath(RespawnariseMod.MOD_ID,
                    "textures/gui/toggle/toggle_off.png");

    private boolean value;
    private final Consumer<Boolean> onToggle;

    private ToggleWidget(int x, int y, int width, int height, Component message, boolean value, Consumer<Boolean> onToggle) {
        super(x, y, width, height, message);
        this.value = value;
        this.onToggle = onToggle;
    }
    public ToggleWidget(int x, int y, int width, int height,boolean value, Consumer<Boolean> onToggle) {
        this(x,y,width,height,Component.literal("toggle"),value,onToggle);
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.blit(
                value ? TEXTURE_ON : TEXTURE_OFF,
                getX(), getY(),
                0, 0,
                width, height,
                width, height
        );
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean result = super.mouseClicked(mouseX, mouseY, button);
        if(result){
            setValue(!value);
            onToggle.accept(value);
        }
        return result;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        return;
    }
}
