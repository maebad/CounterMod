package counter;

import net.fabricmc.loader.api.FabricLoader;
import com.google.gson.Gson;
import java.io.Reader;
import java.io.Writer;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Path;
import java.util.Locale;

public class ConfigHolder {
    // Whether the overlay is visible (toggled with L)
    public static boolean overlayVisible = true;

    // Specific species name (French) to filter, null for global
    public static String specificFrenchName = "";
    public static String overlaySpeciesName = null;

    // Overlay positioning
    public static HorizontalAlign horizontalAlign = HorizontalAlign.LEFT;
    public static VerticalAlign verticalAlign = VerticalAlign.TOP;

    // Text styling
    public static String textColor = "#FFFFFF";
    public static double fontScale = 1.0;
    public static double shadowOpacity = 0.25;
    public static double textOpacity = 1.0;

    // Config file
    private static final Path CONFIG_FILE = FabricLoader.getInstance()
        .getConfigDir().resolve("countermod_config.json");
    private static final Gson GSON = new Gson();

    static {
        loadConfig();
    }

    public static void loadConfig() {
        if (CONFIG_FILE.toFile().exists()) {
            try (Reader r = new FileReader(CONFIG_FILE.toFile())) {
                ConfigData d = GSON.fromJson(r, ConfigData.class);
                overlayVisible = d.overlayVisible;
                specificFrenchName = d.specificFrenchName;
                overlaySpeciesName = d.overlaySpeciesName;
                horizontalAlign = HorizontalAlign.valueOf(d.horizontalAlign);
                verticalAlign = VerticalAlign.valueOf(d.verticalAlign);
                textColor = d.textColor;
                fontScale = d.fontScale;
                shadowOpacity = d.shadowOpacity;
                textOpacity = d.textOpacity;
            } catch (Exception e) {
                // ignore parse errors
            }
        }
    }

    public static void saveConfig() {
        ConfigData d = new ConfigData();
        d.overlayVisible = overlayVisible;
        d.specificFrenchName = specificFrenchName;
        d.overlaySpeciesName = overlaySpeciesName;
        d.horizontalAlign = horizontalAlign.name();
        d.verticalAlign = verticalAlign.name();
        d.textColor = textColor;
        d.fontScale = fontScale;
        d.shadowOpacity = shadowOpacity;
        d.textOpacity = textOpacity;
        try (Writer w = new FileWriter(CONFIG_FILE.toFile())) {
            GSON.toJson(d, w);
        } catch (Exception e) {
            // ignore write errors
        }
    }

    private static class ConfigData {
        boolean overlayVisible;
        String specificFrenchName;
        String overlaySpeciesName;
        String horizontalAlign;
        String verticalAlign;
        String textColor;
        double fontScale;
        double shadowOpacity;
        double textOpacity;
    }
}
