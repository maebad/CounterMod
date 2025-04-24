package counter;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.Minecraft;
import java.awt.Color;

public class RencontresOverlay {
    private static float opacity = 0f;
    private static final float FADE = 0.05f;

    public static void init() {
        HudRenderCallback.EVENT.register((graphics, tickDelta) -> {
            // Reload config each frame
            ConfigHolder.loadConfig();

            // Fade logic
            if (ConfigHolder.overlayVisible && opacity < 1f) {
                opacity = Math.min(1f, opacity + FADE);
            } else if (!ConfigHolder.overlayVisible && opacity > 0f) {
                opacity = Math.max(0f, opacity - FADE);
            }
            if (opacity <= 0f) return;

            Minecraft mc = Minecraft.getInstance();
            var font = mc.font;
            int screenW = graphics.guiWidth();
            int screenH = graphics.guiHeight();
            double scale = ConfigHolder.fontScale;
            int uw = (int)(screenW / scale);
            int uh = (int)(screenH / scale);

            var pose = graphics.pose();
            pose.pushPose();
            pose.translate(0, 0, 200);
            pose.scale((float) scale, (float) scale, 1f);

            int x, y;
            // Determine starting Y based on vertical alignment
            int totalLines = ConfigHolder.showTotal ? 1 : 0;
            for (String sp : ConfigHolder.trackedSpecies) {
                if (sp != null && !sp.isEmpty()) totalLines++;
            }
            int totalHeight = totalLines * font.lineHeight + (totalLines - 1) * 2;

            switch (ConfigHolder.verticalAlign) {
                case TOP -> y = 5;
                case MIDDLE -> y = (uh - totalHeight) / 2;
                case BOTTOM -> y = uh - totalHeight - 5;
                default -> y = 5;
            }

            // Draw the lines
            if (ConfigHolder.showTotal) {
                String text = "Rencontres totales : " + RencontresTracker.total;
                int textW = font.width(text);
                switch (ConfigHolder.horizontalAlign) {
                    case LEFT -> x = 5;
                    case CENTER -> x = (uw - textW) / 2;
                    case RIGHT -> x = uw - textW - 5;
                    default -> x = 5;
                }
                Color shadow = new Color(0, 0, 0, (int)(ConfigHolder.shadowOpacity * opacity * 255));
                Color colorBase = Color.decode(ConfigHolder.textColor);
                Color textColor = new Color(
                    colorBase.getRed(), colorBase.getGreen(), colorBase.getBlue(),
                    (int)(ConfigHolder.textOpacity * opacity * 255)
                );
                graphics.drawString(font, text, x+1, y+1, shadow.getRGB(), false);
                graphics.drawString(font, text, x, y, textColor.getRGB(), false);
                y += font.lineHeight + 2;
            }

            for (String sp : ConfigHolder.trackedSpecies) {
                if (sp != null && !sp.isEmpty()) {
                    String disp = sp.substring(0,1).toUpperCase() + sp.substring(1);
                    String text = "Rencontres " + disp + " : " + RencontresTracker.getCount(sp);
                    int textW = font.width(text);
                    switch (ConfigHolder.horizontalAlign) {
                        case LEFT -> x = 5;
                        case CENTER -> x = (uw - textW) / 2;
                        case RIGHT -> x = uw - textW - 5;
                        default -> x = 5;
                    }
                    Color shadow = new Color(0, 0, 0, (int)(ConfigHolder.shadowOpacity * opacity * 255));
                    Color colorBase = Color.decode(ConfigHolder.textColor);
                    Color textColor = new Color(
                        colorBase.getRed(), colorBase.getGreen(), colorBase.getBlue(),
                        (int)(ConfigHolder.textOpacity * opacity * 255)
                    );
                    graphics.drawString(font, text, x+1, y+1, shadow.getRGB(), false);
                    graphics.drawString(font, text, x, y, textColor.getRGB(), false);
                    y += font.lineHeight + 2;
                }
            }

            pose.popPose();
        });
    }
}
