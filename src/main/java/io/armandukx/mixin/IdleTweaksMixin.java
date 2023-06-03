/*
 * IdleTweaks - Enhances performance while Minecraft runs in the background
 * Copyright (c) 2023 Armandukx
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.armandukx.mixin;

import io.armandukx.IdleTweaks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.settings.GameSettings;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class IdleTweaksMixin {

    @Shadow
    public GameSettings gameSettings;

    @Inject(method = "updateDisplay", at = @At("HEAD"))
    private void onUpdateDisplay(CallbackInfo callbackInfo) {
        if (Display.isCloseRequested() || Minecraft.getMinecraft().theWorld == null) return;
        new Thread(() -> Minecraft.getMinecraft().addScheduledTask(() -> {
            if (!Display.isActive()) {
                if (IdleTweaks.config.bFpsToggle) {
                    gameSettings.limitFramerate = Integer.parseInt(IdleTweaks.config.backgroundFps);
                }
                if (IdleTweaks.config.bDistToggle) {
                    gameSettings.renderDistanceChunks = Integer.parseInt(IdleTweaks.config.backgroundRenderDist);
                }
            } else if (Display.isActive() && Minecraft.getMinecraft().currentScreen instanceof GuiOptions) {
                System.out.println("In Settings"); // For Debugging (I know it's not ideal)
            } else {
                if (IdleTweaks.config.bFpsToggle) {
                    gameSettings.limitFramerate = IdleTweaks.fps;
                }
                if (IdleTweaks.config.bDistToggle) {
                    gameSettings.renderDistanceChunks = IdleTweaks.renderDistance;
                }
            }
        })).start();
    }

    @Inject(method = "runTick", at = @At("RETURN"))
    private void onRunTick(CallbackInfo callbackInfo) {
        if (!Display.isActive()) return;
        int currentFps = gameSettings.limitFramerate;
        int currentDist = gameSettings.renderDistanceChunks;

        if (currentFps != IdleTweaks.fps && (IdleTweaks.fps = getUpdatedValue(currentFps, Integer.parseInt(IdleTweaks.config.backgroundFps))) != currentFps) {
            gameSettings.saveOptions();
        }

        if (currentDist != IdleTweaks.renderDistance && (IdleTweaks.renderDistance = getUpdatedValue(currentDist, Integer.parseInt(IdleTweaks.config.backgroundRenderDist))) != currentDist) {
            gameSettings.saveOptions();
        }
    }

    private int getUpdatedValue(int currentValue, int targetValue) {
        if (currentValue > targetValue) {
            return Math.max(targetValue + 1, currentValue);
        } else if (currentValue < targetValue) {
            return Math.min(targetValue - 1, currentValue);
        } else {
            return currentValue;
        }
    }
}