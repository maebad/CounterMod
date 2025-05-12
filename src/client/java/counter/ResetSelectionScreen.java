package counter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ResetSelectionScreen extends Screen {
    public ResetSelectionScreen() {
        super(Component.literal("Reset Options"));
    }

    @Override
    protected void init() {
        int cx = this.width / 2;
        int y  = this.height / 2 - 30;
        int spacing = 24;

        addRenderableWidget(Button.builder(
            Component.literal("Reset all"),
            b -> {
                RencontresTracker.reset();
                Minecraft.getInstance().player.displayClientMessage(
                  Component.literal("✅ Tous les compteurs ont été réinitialisés"),
                  false
                );
            }
        ).bounds(cx - 100, y, 200, 20).build());
        y += spacing;

        addRenderableWidget(Button.builder(
            Component.literal("Reset total"),
            b -> {
                RencontresTracker.resetTotal();
                Minecraft.getInstance().player.displayClientMessage(
                  Component.literal("✅ Compteur total réinitialisé"),
                  false
                );
            }
        ).bounds(cx - 100, y, 200, 20).build());
        y += spacing;

        addRenderableWidget(Button.builder(
            Component.literal("Reset Pokémon"),
            b -> Minecraft.getInstance().setScreen(new ResetSpeciesScreen())
        ).bounds(cx - 100, y, 200, 20).build());
        y += spacing;

        addRenderableWidget(Button.builder(
            Component.literal("Back"),
            b -> Minecraft.getInstance().setScreen(new ConfigScreen())
        ).bounds(cx - 100, y, 200, 20).build());
    }

    @Override
    public void render(GuiGraphics g, int mx, int my, float delta) {
        this.renderBackground(g, mx, my, delta);
        super.render(g, mx, my, delta);
        String title = "Reset Options";
        int tw = this.font.width(title);
        g.drawString(this.font, title, (this.width - tw)/2, 10, 0xFFFFFF, false);
    }
}