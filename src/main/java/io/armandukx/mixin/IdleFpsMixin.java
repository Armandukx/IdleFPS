/*
 * IdleFPS - Limit FPS when Minecraft is in the background
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

import io.armandukx.IdleFPS;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class IdleFpsMixin {
    @Shadow public abstract void setIngameFocus();
    @Shadow public abstract void setIngameNotInFocus();

    @Inject(method = "updateDisplay", at = @At("HEAD"))
    private void onUpdateDisplay(CallbackInfo callbackInfo) {
        if (!Display.isActive()) {
            setIngameNotInFocus();
            GameSettings gameSettings = Minecraft.getMinecraft().gameSettings;
            gameSettings.limitFramerate = 1;
        }
    }

    @Inject(method = "displayGuiScreen", at = @At("RETURN"))
    private void onDisplayGuiScreen(GuiScreen guiScreen, CallbackInfo callbackInfo) {
        if (guiScreen == null && Display.isActive()) {
            setIngameFocus();
            GameSettings gameSettings = Minecraft.getMinecraft().gameSettings;
            gameSettings.limitFramerate = IdleFPS.fps;
        }
    }

    @Inject(method = "runTick", at = @At("RETURN"))
    private void onRunTick(CallbackInfo callbackInfo){
        if (!Display.isActive()) return;
        GameSettings gameSettings = Minecraft.getMinecraft().gameSettings;
        int currentFps = gameSettings.limitFramerate;
        if (currentFps != IdleFPS.fps) {
            if(currentFps >= 10){
                IdleFPS.fps = currentFps;
                gameSettings.saveOptions();
            }
        }
    }
}