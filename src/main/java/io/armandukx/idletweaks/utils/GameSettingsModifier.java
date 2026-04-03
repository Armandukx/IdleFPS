package io.armandukx.idletweaks.utils;

import io.armandukx.idletweaks.IdleTweaks;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.option.GameOptions;

public class GameSettingsModifier {
    public static boolean idleActive = false;
    private static long lastFocusLoss = 0;

    public static void init() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (IdleTweaks.closing || client.world == null) return;

            GameOptions gameSettings = client.options;
            Screen current = client.currentScreen;

            boolean focused = client.isWindowFocused();
            long now = System.currentTimeMillis();

            if (!focused) {
                if (lastFocusLoss == 0) {
                    lastFocusLoss = now;
                }

                long elapsed = now - lastFocusLoss;
                if (!idleActive && elapsed >= IdleTweaks.getConfig().Cooldown * 1000L) {
                    enableIdleMode(client, gameSettings);
                }
            } else {
                // Only reset lastFocusLoss if it was set before
                if (lastFocusLoss != 0) lastFocusLoss = 0;

                // Deactivate idle mode if currently active and not in any "protected" screen
                if (idleActive && (!(current instanceof OptionsScreen))) {
                    disableIdleMode(client, gameSettings);
                }
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.world == null || !client.isWindowFocused()) return;

            GameOptions options = client.options;

            int currentDist = options.getViewDistance().getValue();

            if (!idleActive && currentDist != IdleTweaks.renderDistance) {
                IdleTweaks.renderDistance = currentDist;
            }
        });
    }

    private static void enableIdleMode(MinecraftClient client, GameOptions gameSettings) {
        if (IdleTweaks.getConfig().bDistToggle) {
            gameSettings.getViewDistance().setValue(IdleTweaks.getConfig().backgroundRenderDist);
        }
        if (IdleTweaks.getConfig().bVolumeToggle) {
            client.getSoundManager().stopAll();
        }
        idleActive = true;
    }

    private static void disableIdleMode(MinecraftClient client, GameOptions gameSettings) {
        if (IdleTweaks.getConfig().bDistToggle) {
            gameSettings.getViewDistance().setValue(IdleTweaks.renderDistance);
        }
        if (IdleTweaks.getConfig().bVolumeToggle) {
            client.getSoundManager().resumeAll();
        }
        idleActive = false;
    }
}