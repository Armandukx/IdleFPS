package io.armandukx.idletweaks.utils;

import io.armandukx.idletweaks.IdleTweaks;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.sound.SoundCategory;
public class GameSettingsModifier {

    public static void init(){
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (IdleTweaks.closing) return;

            if (MinecraftClient.getInstance().getWindow().shouldClose() || MinecraftClient.getInstance().world == null)
                return;
            GameOptions gameSettings = MinecraftClient.getInstance().options;
            if (!MinecraftClient.getInstance().isWindowFocused()) {
                if (IdleTweaks.getConfig().bFpsToggle) {
                    setFpsLimit(IdleTweaks.getConfig().backgroundFps);
                }
                if (IdleTweaks.getConfig().bDistToggle) {
                    gameSettings.getViewDistance().setValue(IdleTweaks.getConfig().backgroundRenderDist);
                }
                if (IdleTweaks.getConfig().bVolumeToggle) {
                    MinecraftClient.getInstance().getSoundManager().stopAll();
                }
            } else {
                if (MinecraftClient.getInstance().currentScreen != null && MinecraftClient.getInstance().currentScreen instanceof VideoOptionsScreen) {
                    return;
                } else {
                    if (IdleTweaks.getConfig().bFpsToggle) {
                        setFpsLimit(IdleTweaks.fps);
                    }
                    if (IdleTweaks.getConfig().bDistToggle) {
                        gameSettings.getViewDistance().setValue(IdleTweaks.renderDistance);
                    }
                }
                if (IdleTweaks.getConfig().bVolumeToggle) {
                    if (gameSettings.getSoundVolume(SoundCategory.MASTER) <= 0) {
                        MinecraftClient.getInstance().getSoundManager().resumeAll();
                    }
                }
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.world != null) {
                if (!MinecraftClient.getInstance().isWindowFocused()) return;

                int currentDist = IdleTweaks.gameSettings.getViewDistance().getValue();
                int currentFps = IdleTweaks.gameSettings.getMaxFps().getValue();

                if (currentDist != IdleTweaks.renderDistance && (currentDist >= IdleTweaks.getConfig().backgroundRenderDist + 1) || (currentDist <= IdleTweaks.getConfig().backgroundRenderDist - 1)) {
                    IdleTweaks.renderDistance = currentDist;
                }

                if (currentFps != IdleTweaks.fps && (currentFps >= IdleTweaks.getConfig().backgroundFps + 1) || (currentFps <= IdleTweaks.getConfig().backgroundFps - 1)) {
                    IdleTweaks.fps = currentFps;
                }
                IdleTweaks.gameSettings.write();
            }
        });
    }
    public static void setFpsLimit(int fps) {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        minecraft.getWindow().setFramerateLimit(fps);
    }
}
