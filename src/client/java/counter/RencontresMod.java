package counter;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;

public class RencontresMod implements ClientModInitializer {
    public static KeyMapping toggleKey, configKey;

    @Override
    public void onInitializeClient() {
        // Load config preferences so they apply immediately
        ConfigHolder.loadConfig();

        // Initialize key bindings
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.rencontresmod.toggle",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_L,
            "category.rencontresmod.controls"
        ));
        configKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.rencontresmod.config",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_K,
            "category.rencontresmod.controls"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.consumeClick()) {
                ConfigHolder.overlayVisible = !ConfigHolder.overlayVisible;
            }
            while (configKey.consumeClick()) {
                // Reload config before opening settings
                ConfigHolder.loadConfig();
                Minecraft.getInstance().setScreen(new ConfigScreen());
            }
        });

        RencontresTracker.init();
        RencontresOverlay.init();
        CountCommand.register();
        ResetCommand.register();
    }
}
