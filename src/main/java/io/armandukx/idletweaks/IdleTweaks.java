package io.armandukx.idletweaks;

import io.armandukx.idletweaks.command.IDTCommand;
import io.armandukx.idletweaks.config.Config;
import io.armandukx.idletweaks.utils.GameSettingsModifier;
import io.armandukx.idletweaks.utils.UpdateChecker;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Formatting;

public class IdleTweaks implements ClientModInitializer {
    public static final String VERSION = "1.2.1";
    public static final String prefix =
            Formatting.YELLOW + "[I" + Formatting.GREEN + "D" + Formatting.RED + "T] " + Formatting.RESET;
    public static int RenderDistance = 0;
    public static boolean closing = false;
    private static Config config;
    public static boolean _STOPCHECKING = false;

    @Override
    public void onInitializeClient() {
        config = new Config();
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, registrationEnvironment) -> IDTCommand.register(dispatcher));
        GameSettingsModifier.init();

        if (config.bDistToggle && (config.backgroundRenderDist == 0 || config.backgroundRenderDist == 1)) {
            config.setBackgroundRenderDist(2);
        }

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            RenderDistance = client.options.getViewDistance().getValue();
            GameSettingsModifier.IdleActive = false;
            GameSettingsModifier.LastFocusLoss = 0;
        });

        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
            System.out.println("Minecraft is closing");

            GameOptions GameSettings = client.options;
            if (GetConfig().bDistToggle) {
                GameSettings.getViewDistance().setValue(IdleTweaks.RenderDistance);
                GameSettings.write();
                System.out.println(GameSettings.getViewDistance().getValue());
            }
            if (GetConfig().bVolumeToggle) {
                if (GameSettings.getSoundVolume(SoundCategory.MASTER) <= 0) {
                    MinecraftClient.getInstance().getSoundManager().resumeAll();
                }
            }
            closing = true;
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!_STOPCHECKING && client.player != null && client.world != null) {
                _STOPCHECKING = true;
                new Thread(UpdateChecker::check).start();
            }
        });
    }
    public static Config GetConfig() {
        return config;
    }
}
