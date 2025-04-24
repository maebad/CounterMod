package counter;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.network.chat.Component;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class ResetCommand {
    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((disp, env) ->
            disp.register(literal("resetcount").executes(ctx -> {
                RencontresTracker.reset();
                ctx.getSource().getPlayer().sendSystemMessage(
                    Component.literal("Compteurs réinitialisés"));
                return 1;
            }))
        );
    }
}
