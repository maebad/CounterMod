package counter;

import net.fabricmc.loader.api.FabricLoader;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.Writer;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ConfigHolder {
    public static boolean overlayVisible = true;
    public static boolean showTotal = true;
    public static List<String> trackedSpecies = new ArrayList<>(List.of("", "", "", ""));
    public static HorizontalAlign horizontalAlign = HorizontalAlign.LEFT;
    public static VerticalAlign verticalAlign = VerticalAlign.TOP;

    public static final String[] COLOR_OPTIONS = {
        "#FFFFFF", "#FF5555", "#55FF55", "#5555FF",
        "#FFFF55", "#FF55FF", "#55FFFF", "#AAAAAA"
    };
    public static final double[] SCALE_OPTIONS = {0.5, 0.75, 1.0, 1.25, 1.5};
    public static final double[] SHADOW_OPTIONS = {0.25, 0.5, 0.75, 1.0};
    public static final double[] OPACITY_OPTIONS = {0.3, 0.5, 0.7, 1.0};

    public static int colorIndex = 0;
    public static int scaleIndex = 2;
    public static int shadowIndex = 0;
    public static int opacityIndex = 3;

    public static String textColor = COLOR_OPTIONS[colorIndex];
    public static double fontScale = SCALE_OPTIONS[scaleIndex];
    public static double shadowOpacity = SHADOW_OPTIONS[shadowIndex];
    public static double textOpacity = OPACITY_OPTIONS[opacityIndex];

    private static final Path CONFIG_FILE =
        FabricLoader.getInstance().getConfigDir().resolve("countermod_config.json");
    private static final Gson GSON = new Gson();

    static { loadConfig(); }

    public static void loadConfig() {
        if (!CONFIG_FILE.toFile().exists()) return;
        try {
            String json = Files.readString(CONFIG_FILE, StandardCharsets.UTF_8);
            JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
            ConfigData d = GSON.fromJson(json, ConfigData.class);

            overlayVisible = d.overlayVisible;
            horizontalAlign = HorizontalAlign.valueOf(d.horizontalAlign);
            verticalAlign = VerticalAlign.valueOf(d.verticalAlign);

            // textColor and index
            if (d.textColor != null) {
                textColor = d.textColor;
                for (int i = 0; i < COLOR_OPTIONS.length; i++) {
                    if (COLOR_OPTIONS[i].equals(textColor)) {
                        colorIndex = i; break;
                    }
                }
            }
            // fontScale and index
            fontScale = d.fontScale;
            for (int i = 0; i < SCALE_OPTIONS.length; i++) {
                if (SCALE_OPTIONS[i] == fontScale) {
                    scaleIndex = i; break;
                }
            }
            // shadowOpacity and index
            shadowOpacity = d.shadowOpacity;
            for (int i = 0; i < SHADOW_OPTIONS.length; i++) {
                if (SHADOW_OPTIONS[i] == shadowOpacity) {
                    shadowIndex = i; break;
                }
            }
            // textOpacity and index
            textOpacity = d.textOpacity;
            for (int i = 0; i < OPACITY_OPTIONS.length; i++) {
                if (OPACITY_OPTIONS[i] == textOpacity) {
                    opacityIndex = i; break;
                }
            }

            if (obj.has("showTotal")) {
                showTotal = d.showTotal;
            }
            if (obj.has("trackedSpecies") && d.trackedSpecies != null) {
                trackedSpecies = new ArrayList<>(d.trackedSpecies);
            } else if (obj.has("specificFrenchName") && d.specificFrenchName != null && !d.specificFrenchName.isEmpty()) {
                trackedSpecies.set(0, d.specificFrenchName);
            }
        } catch (Exception ignored) {}
    }

    public static void saveConfig() {
        ConfigData d = new ConfigData();
        d.overlayVisible  = overlayVisible;
        d.showTotal       = showTotal;
        d.trackedSpecies  = trackedSpecies;
        d.horizontalAlign = horizontalAlign.name();
        d.verticalAlign   = verticalAlign.name();
        d.textColor       = textColor;
        d.fontScale       = fontScale;
        d.shadowOpacity   = shadowOpacity;
        d.textOpacity     = textOpacity;
        try (Writer w = new FileWriter(CONFIG_FILE.toFile())) {
            GSON.toJson(d, w);
        } catch (Exception ignored) {}
    }

    private static class ConfigData {
        boolean overlayVisible;
        boolean showTotal;
        List<String> trackedSpecies;
        String horizontalAlign;
        String verticalAlign;
        String textColor;
        double fontScale;
        double shadowOpacity;
        double textOpacity;
        String specificFrenchName;
    }
}
