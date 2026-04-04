package io.armandukx.idletweaks.utils;

import io.armandukx.idletweaks.IdleTweaks;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.option.GameOptions;

public class GameSettingsModifier {
    public static boolean IdleActive = false;
    public static long LastFocusLoss = 0;

    public static void init() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (IdleTweaks.closing || client.world == null) return;

            GameOptions GameSettings = client.options;
            Screen current = client.currentScreen;

            boolean focused = client.isWindowFocused();
            long now = System.currentTimeMillis();

            if (!focused) {
                if (LastFocusLoss == 0) {
                    LastFocusLoss = now;
                }

                long elapsed = now - LastFocusLoss;
                if (!IdleActive && elapsed >= IdleTweaks.GetConfig().Cooldown * 1000L) {
                    EnableIdleMode(client, GameSettings);
                }
            } else {
                if (LastFocusLoss != 0) LastFocusLoss = 0;

                if (IdleActive && (!(current instanceof OptionsScreen))) {
                    DisableIdleMode(client, GameSettings);
                }
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.world == null || !client.isWindowFocused()) return;

            GameOptions options = client.options;

            int currentDist = options.getViewDistance().getValue();

            if (!IdleActive && currentDist != IdleTweaks.RenderDistance) {
                IdleTweaks.RenderDistance = currentDist;
            }
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            if (IdleActive) {
                DisableIdleMode(client, client.options);
            }
            LastFocusLoss = 0;
        });
    }

    private static void EnableIdleMode(MinecraftClient client, GameOptions GameSettings) {
        if (IdleTweaks.GetConfig().bDistToggle) {
            GameSettings.getViewDistance().setValue(IdleTweaks.GetConfig().backgroundRenderDist);
        }
        if (IdleTweaks.GetConfig().bVolumeToggle) {
            client.getSoundManager().stopAll();
        }
        IdleActive = true;
    }

    public static void DisableIdleMode(MinecraftClient client, GameOptions GameSettings) {
        if (IdleTweaks.GetConfig().bDistToggle) {
            GameSettings.getViewDistance().setValue(IdleTweaks.RenderDistance);
        }
        if (IdleTweaks.GetConfig().bVolumeToggle) {
            client.getSoundManager().resumeAll();
        }
        IdleActive = false;
    }
}