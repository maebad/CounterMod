package counter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import java.util.Locale;

public class SpecificSpeciesScreen extends Screen {
    private final int index;
    private EditBox input;

    public SpecificSpeciesScreen(int idx) {
        super(Component.literal("Pokémon Spécifique " + (idx + 1)));
        this.index = idx;
    }

    @Override
    protected void init() {
        super.init();
        ConfigHolder.loadConfig();
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        // Input field
        input = new EditBox(this.font, centerX - 100, centerY - 10, 200, 20, Component.literal(""));
        input.setValue(ConfigHolder.trackedSpecies.get(index));
        addRenderableWidget(input);

        // Done button
        addRenderableWidget(
            Button.builder(Component.literal("Done"), b -> {
                ConfigHolder.trackedSpecies.set(index, input.getValue().trim().toLowerCase(Locale.ROOT));
                ConfigHolder.saveConfig();
                Minecraft.getInstance().setScreen(new ConfigScreen());
            }).bounds(centerX - 40, centerY + 20, 80, 20).build()
        );
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float delta) {
        this.renderBackground(g, mouseX, mouseY, delta);
        super.render(g, mouseX, mouseY, delta);
        // Title label above input
        String label = "Pokémon Spécifique " + (index + 1);
        int lw = this.font.width(label);
        g.drawString(this.font, label, (this.width - lw) / 2, this.height / 2 - 30, 0xFFFFFF, false);
    }
}
