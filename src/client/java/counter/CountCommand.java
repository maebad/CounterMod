package counter;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.Locale;

public class CountCommand {
    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(
                ClientCommandManager.literal("count")
                    .then(ClientCommandManager.argument("species", StringArgumentType.word())
                        .executes(ctx -> {
                            String raw = StringArgumentType.getString(ctx, "species")
                                .toLowerCase(Locale.ROOT);
                            int cnt = RencontresTracker.getCount(raw);
                            // Affiche localement le message dans le chat client
                            Minecraft.getInstance().player.displayClientMessage(
                                Component.literal("Rencontres pour " + raw + " : " + cnt), false
                            );
                            return cnt;
                        })
                    )
            );
        });
    }
}
