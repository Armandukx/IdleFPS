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

package io.armandukx;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(
        name = IdleFPS.NAME,
        modid = IdleFPS.MODID,
        version = IdleFPS.VERSION
)
public class IdleFPS {
    public static final String NAME = "IdleFPS";
    public static final String MODID = "idlefps";
    public static final String VERSION = "1.0.0";
    private boolean fpsRetrieved = false;
    public static int fps;
    @Mod.EventHandler
    public void init() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent event) {
        if (event.type == RenderGameOverlayEvent.ElementType.TEXT && !fpsRetrieved) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.currentScreen == null) {
                fps = Minecraft.getDebugFPS();
                fpsRetrieved = true;
            }
        }
    }
}
