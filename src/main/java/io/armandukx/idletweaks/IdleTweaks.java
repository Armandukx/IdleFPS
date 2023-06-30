package io.armandukx.idletweaks;

import io.armandukx.idletweaks.command.IDTCommand;
import io.armandukx.idletweaks.config.Config;
import io.armandukx.idletweaks.utils.GameSettingsModifier;
import io.armandukx.idletweaks.utils.UpdateChecker;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Formatting;

public class IdleTweaks implements ClientModInitializer {
    public static final String NAME = "IdleTweaks";
    public static final String VERSION = "1.0.6";
    public static final String prefix =
            Formatting.YELLOW + "[I" + Formatting.GREEN + "D" + Formatting.RED + "T] " + Formatting.RESET;
    public static int renderDistance = 0;
    public static int fps = 10;
    private static boolean retrieved = false;
    public static boolean closing = false;
    public static GameOptions gameSettings;
    private static Config config;

    @Override
    public void onInitializeClient() {
        config = new Config();
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> IDTCommand.register(dispatcher));
        GameSettingsModifier.init();
        UpdateChecker.init();

        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            gameSettings = MinecraftClient.getInstance().options;
            if (!retrieved) {
                fps = gameSettings.maxFps;
                renderDistance = gameSettings.viewDistance;
                retrieved = true; // Just being careful
                System.out.println(prefix + "Current Fps: " + fps + " Current Render Distance: " + renderDistance);
            }
        });

        ClientLifecycleEvents.CLIENT_STOPPING.register(server -> {
            System.out.println("Minecraft is closing");

            if (getConfig().bFpsToggle) {
                GameSettingsModifier.setFpsLimit(fps);
                gameSettings.write();
                System.out.println(gameSettings.maxFps);
            }
            if (getConfig().bDistToggle) {
                gameSettings.viewDistance = IdleTweaks.renderDistance;
                gameSettings.write();
                System.out.println(gameSettings.viewDistance);
            }
            if (getConfig().bVolumeToggle) {
                if (gameSettings.getSoundVolume(SoundCategory.MASTER) <= 0) {
                    MinecraftClient.getInstance().getSoundManager().resumeAll();
                }
            }
            closing = true;
        });
    }
    public static Config getConfig() {
        return config;
    }
}
