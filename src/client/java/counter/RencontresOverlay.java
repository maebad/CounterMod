package counter;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.Minecraft;
import java.awt.Color;

public class RencontresOverlay {
    private static float opacity = 0f;
    private static final float FADE = 0.05f;

    public static void init() {
        // Apply config before registering overlay
        ConfigHolder.loadConfig();

        HudRenderCallback.EVENT.register((ctx, delta) -> {
            if (ConfigHolder.overlayVisible && opacity < 1f) {
                opacity = Math.min(1f, opacity + FADE);
            } else if (!ConfigHolder.overlayVisible && opacity > 0f) {
                opacity = Math.max(0f, opacity - FADE);
            }

            if (opacity <= 0f) return;

            GuiGraphics g = ctx;
            int screenWidth = g.guiWidth();
            int screenHeight = g.guiHeight();
            double scale = ConfigHolder.fontScale;
            int widthUnscaled = (int)(screenWidth / scale);
            int heightUnscaled = (int)(screenHeight / scale);

            String key = ConfigHolder.overlaySpeciesName;
            String displayName;
            if (key == null) {
                displayName = "totales";
            } else if (key.isEmpty()) {
                displayName = "";
            } else {
                displayName = key.substring(0, 1).toUpperCase() + key.substring(1);
            }
            String label = (key == null) ? "Rencontres totales : " : "Rencontres " + displayName + " : ";
            int cnt = (key == null) ? RencontresTracker.total : RencontresTracker.getCount(key);
            String text = label + cnt;

            int x = switch(ConfigHolder.horizontalAlign) {
                case LEFT -> 5;
                case CENTER -> (widthUnscaled - Minecraft.getInstance().font.width(text)) / 2;
                case RIGHT -> (widthUnscaled - Minecraft.getInstance().font.width(text) - 5);
            };
            int yPos = switch(ConfigHolder.verticalAlign) {
                case TOP -> 5;
                case MIDDLE -> (heightUnscaled - Minecraft.getInstance().font.lineHeight) / 2;
                case BOTTOM -> (heightUnscaled - Minecraft.getInstance().font.lineHeight - 5);
            };

            g.pose().pushPose();
            g.pose().translate(0, 0, 200);
            g.pose().scale((float)scale, (float)scale, 1f);

            // Shadow with configurable opacity
            Color sc = new Color(0, 0, 0, (int)(ConfigHolder.shadowOpacity * opacity * 255));
            // Draw shadow offset
            g.drawString(Minecraft.getInstance().font, text, x + 1, yPos + 1, sc.getRGB(), false);
            // Main text color
            var base = Color.decode(ConfigHolder.textColor);
            Color tc = new Color(base.getRed(), base.getGreen(), base.getBlue(),
                                 (int)(ConfigHolder.textOpacity * opacity * 255));
            g.drawString(Minecraft.getInstance().font, text, x, yPos, tc.getRGB(), false);

            g.pose().popPose();
        });
    }
}
