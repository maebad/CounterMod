package counter;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.network.chat.Component;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class ResetCommand {
    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, env) -> {
            // /resetall  -> comme avant : tout reset
            dispatcher.register(
                literal("resetall")
                    .executes(ctx -> {
                        RencontresTracker.reset();
                        ctx.getSource().getPlayer().sendSystemMessage(
                            Component.literal("✅ Tous les compteurs ont été réinitialisés"));
                        return 1;
                    })
            );

            // /resetcount <espèce>  -> ne reset qu'une seule espèce
            dispatcher.register(
                literal("resetcount")
                    .then(argument("species", StringArgumentType.word())
                        .executes(ctx -> {
                            String sp = StringArgumentType.getString(ctx, "species");
                            RencontresTracker.resetSpecies(sp);
                            ctx.getSource().getPlayer().sendSystemMessage(
                                Component.literal("✅ Compteur de « " + sp + " » réinitialisé"));
                            return 1;
                        })
                    )
            );

            // /resettotal  -> ne reset que le total
            dispatcher.register(
                literal("resettotal")
                    .executes(ctx -> {
                        RencontresTracker.resetTotal();
                        ctx.getSource().getPlayer().sendSystemMessage(
                            Component.literal("✅ Compteur total réinitialisé"));
                        return 1;
                    })
            );
        });
    }
}