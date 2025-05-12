package counter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import java.util.Locale;

public class ResetSpeciesScreen extends Screen {
    private EditBox input;

    public ResetSpeciesScreen() {
        super(Component.literal("Reset Pokémon"));
    }

    @Override
    protected void init() {
        int cx = this.width / 2;
        int cy = this.height / 2;
        int w = 200, h = 20, spacing = 24;

        input = new EditBox(this.font, cx - w/2, cy - h - 10, w, h, Component.literal("Pokémon"));
        input.setMaxLength(64);
        input.setValue("");
        this.addRenderableWidget(input);

        addRenderableWidget(Button.builder(
            Component.literal("Confirm"),
            b -> {
                String species = input.getValue().toLowerCase(Locale.ROOT);
                RencontresTracker.resetSpecies(species);
                Minecraft.getInstance().player.displayClientMessage(
                  Component.literal("✅ Compteur de « " + species + " » réinitialisé"),
                  false
                );
            }
        ).bounds(cx - 100, cy + 5, 200, 20).build());

        addRenderableWidget(Button.builder(
            Component.literal("Back"),
            b -> Minecraft.getInstance().setScreen(new ResetSelectionScreen())
        ).bounds(cx - 100, cy + 5 + spacing, 200, 20).build());
    }

    @Override
    public void render(GuiGraphics g, int mx, int my, float delta) {
        this.renderBackground(g, mx, my, delta);
        super.render(g, mx, my, delta);
        String title = "Reset Pokémon";
        int tw = this.font.width(title);
        g.drawString(this.font, title, (this.width - tw)/2, this.height/2 - 50, 0xFFFFFF, false);
    }
}