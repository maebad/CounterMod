package counter;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import java.util.Locale;
import counter.ConfigHolder;
import counter.RencontresTracker;

public class ConfigScreen extends Screen {
    private EditBox speciesInput;
    private final String[] colors = {
        "#FFFFFF","#FF5555","#55FF55","#5555FF",
        "#FFFF55","#FF55FF","#55FFFF","#AAAAAA"
    };
    private final double[] scales = {0.5, 0.75, 1.0, 1.25, 1.5};
    private final double[] shadows = {0.25,0.5,0.75,1.0};
    private final double[] opacities = {0.3,0.5,0.7,1.0};

    public ConfigScreen() {
        super(Component.literal("CounterMod Settings"));
    }

    @Override
    protected void init() {
        super.init();
        // Reload config preferences into UI
        ConfigHolder.loadConfig();

        int y = 40;
        int x = this.width / 2 - 100;

        speciesInput = new EditBox(this.font, x, y, 200, 20, Component.literal(""));
        speciesInput.setMaxLength(50);
        speciesInput.setValue(ConfigHolder.specificFrenchName);
        addRenderableWidget(speciesInput);
        y += 24;

        // Horizontal Align
        Button horiz = Button.builder(
            Component.literal("Horizontal Align: " + ConfigHolder.horizontalAlign.name()),
            btn -> {
                ConfigHolder.horizontalAlign = HorizontalAlign.values()[
                    (ConfigHolder.horizontalAlign.ordinal() + 1) % HorizontalAlign.values().length];
                btn.setMessage(Component.literal("Horizontal Align: " + ConfigHolder.horizontalAlign.name()));
            }
        ).bounds(x, y, 200, 20).build();
        addRenderableWidget(horiz);
        y += 24;

        // Vertical Align
        Button vert = Button.builder(
            Component.literal("Vertical Align: " + ConfigHolder.verticalAlign.name()),
            btn -> {
                ConfigHolder.verticalAlign = VerticalAlign.values()[
                    (ConfigHolder.verticalAlign.ordinal() + 1) % VerticalAlign.values().length];
                btn.setMessage(Component.literal("Vertical Align: " + ConfigHolder.verticalAlign.name()));
            }
        ).bounds(x, y, 200, 20).build();
        addRenderableWidget(vert);
        y += 24;

        // Color
        Button colorBtn = Button.builder(
            Component.literal("Color: " + ConfigHolder.textColor),
            btn -> {
                int idx = java.util.Arrays.asList(colors).indexOf(ConfigHolder.textColor);
                if (idx < 0) idx = 0;
                ConfigHolder.textColor = colors[(idx + 1) % colors.length];
                btn.setMessage(Component.literal("Color: " + ConfigHolder.textColor));
            }
        ).bounds(x, y, 200, 20).build();
        addRenderableWidget(colorBtn);
        y += 24;

        // Scale
        Button scaleBtn = Button.builder(
            Component.literal("Scale: " + ConfigHolder.fontScale),
            btn -> {
                int idx = 0;
                for (int i = 0; i < scales.length; i++) {
                    if (scales[i] == ConfigHolder.fontScale) {
                        idx = i;
                        break;
                    }
                }
                ConfigHolder.fontScale = scales[(idx + 1) % scales.length];
                btn.setMessage(Component.literal("Scale: " + ConfigHolder.fontScale));
            }
        ).bounds(x, y, 200, 20).build();
        addRenderableWidget(scaleBtn);
        y += 24;

        // Shadow
        Button shadowBtn = Button.builder(
            Component.literal("Shadow: " + ConfigHolder.shadowOpacity),
            btn -> {
                int idx = 0;
                for (int i = 0; i < shadows.length; i++) {
                    if (shadows[i] == ConfigHolder.shadowOpacity) {
                        idx = i;
                        break;
                    }
                }
                ConfigHolder.shadowOpacity = shadows[(idx + 1) % shadows.length];
                btn.setMessage(Component.literal("Shadow: " + ConfigHolder.shadowOpacity));
            }
        ).bounds(x, y, 200, 20).build();
        addRenderableWidget(shadowBtn);
        y += 24;

        // Opacity
        Button opacityBtn = Button.builder(
            Component.literal("Opacity: " + ConfigHolder.textOpacity),
            btn -> {
                int idx = 0;
                for (int i = 0; i < opacities.length; i++) {
                    if (opacities[i] == ConfigHolder.textOpacity) {
                        idx = i;
                        break;
                    }
                }
                ConfigHolder.textOpacity = opacities[(idx + 1) % opacities.length];
                btn.setMessage(Component.literal("Opacity: " + ConfigHolder.textOpacity));
            }
        ).bounds(x, y, 200, 20).build();
        addRenderableWidget(opacityBtn);
        y += 24;

        // Reset counter button
        Button resetBtn = Button.builder(
            Component.literal("Réinitialiser le compteur"),
            btn -> RencontresTracker.reset()
        ).bounds(x, y, 200, 20).build();
        addRenderableWidget(resetBtn);
        y += 24;

        // Done
        Button done = Button.builder(
            Component.literal("Done"),
            btn -> {
                String raw = speciesInput.getValue().trim();
                ConfigHolder.specificFrenchName = raw;
                if (raw.isEmpty()) {
                    ConfigHolder.overlaySpeciesName = null;
                } else {
                    ConfigHolder.overlaySpeciesName = raw.toLowerCase(Locale.ROOT);
                }
                ConfigHolder.saveConfig();
                Minecraft.getInstance().setScreen(null);
            }
        ).bounds(x, y, 200, 20).build();
        addRenderableWidget(done);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(graphics, mouseX, mouseY, delta);
        super.render(graphics, mouseX, mouseY, delta);

        String line1 = "Compteur Pokémon Spécifique";
        String line2 = "(Respecter les accents) :";
        int w1 = this.font.width(line1);
        int w2 = this.font.width(line2);
        graphics.drawString(this.font, line1, (this.width - w1) / 2, 10, 0xFFFFFF, false);
        graphics.drawString(this.font, line2, (this.width - w2) / 2, 10 + this.font.lineHeight, 0xFFFFFF, false);
    }

    @Override
    public void onClose() {
        ConfigHolder.saveConfig();
        super.onClose();
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}
