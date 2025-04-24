package counter;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.loader.api.FabricLoader;
import com.google.gson.Gson;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class RencontresTracker {
    public static int total = 0;
    public static Map<String, Integer> speciesCounts = new HashMap<>();
    private static final Set<UUID> seen = new HashSet<>();
    private static final Gson GSON = new Gson();
    private static final Path SAVE_FILE =
        FabricLoader.getInstance().getConfigDir().resolve("countermod_counts.json");

    public static void init() {
        loadCounts();
        // Only count on entity load; do not remove on unload to keep cumulative counts
        ClientEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (!(entity instanceof PokemonEntity)) return;
            PokemonEntity p = (PokemonEntity) entity;
            if (p.getOwnerUUID() == null && seen.add(p.getUUID())) {
                total++;
                String name = p.getName().getString().toLowerCase(Locale.ROOT);
                speciesCounts.put(name, speciesCounts.getOrDefault(name, 0) + 1);
                saveCounts();
            }
        });
    }

    public static void reset() {
        seen.clear();
        total = 0;
        speciesCounts.clear();
        saveCounts();
    }

    public static int getCount(String name) {
        return speciesCounts.getOrDefault(name.toLowerCase(Locale.ROOT), 0);
    }

    public static void loadCounts() {
        if (SAVE_FILE.toFile().exists()) {
            try (Reader r = new FileReader(SAVE_FILE.toFile())) {
                SaveData d = GSON.fromJson(r, SaveData.class);
                total = d.total;
                speciesCounts.clear();
                speciesCounts.putAll(d.speciesCounts);
            } catch (Exception ignored) {}
        }
    }

    public static void saveCounts() {
        try (Writer w = new FileWriter(SAVE_FILE.toFile())) {
            GSON.toJson(new SaveData(total, speciesCounts), w);
        } catch (Exception ignored) {}
    }

    private static class SaveData {
        int total;
        Map<String, Integer> speciesCounts;
        SaveData(int t, Map<String, Integer> m) { total = t; speciesCounts = m; }
    }
}
