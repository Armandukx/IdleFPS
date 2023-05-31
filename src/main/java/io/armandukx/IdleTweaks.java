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

package io.armandukx;

import io.armandukx.command.IFPSCommand;
import io.armandukx.config.Config;
import io.armandukx.config.ConfigHandler;
import io.armandukx.listener.EventListener;
import io.armandukx.utils.UpdateChecker;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(
        name = IdleTweaks.NAME,
        modid = IdleTweaks.MODID,
        version = IdleTweaks.VERSION,
        useMetadata = true
)
public class IdleTweaks {
    public static final String NAME = "IdleTweaks";
    public static final String MODID = "idletweaks";
    public static final String VERSION = "1.0.3";
    public static IdleTweaks instance;
    public static final String prefix =
            EnumChatFormatting.WHITE + "[" + EnumChatFormatting.WHITE + "I" + EnumChatFormatting.YELLOW + "F" + EnumChatFormatting.GREEN + "P" + EnumChatFormatting.RED + "S"
                    + "] " + EnumChatFormatting.RESET;
    private boolean retrieved = false;
    public static int fps;
    public static int renderDistance;
    private final EventListener eventListener;

    public IdleTweaks() {
        this.eventListener = new EventListener();
    }
    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        IdleTweaks.instance = this;
        ClientCommandHandler.instance.registerCommand(new IFPSCommand());
    }
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(eventListener);
        MinecraftForge.EVENT_BUS.register(new UpdateChecker());
        ConfigHandler.reloadConfig();
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onRenderGameOverlay(RenderGameOverlayEvent event) {
        if (event.type == RenderGameOverlayEvent.ElementType.TEXT && !retrieved) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.currentScreen == null) {
                fps = mc.gameSettings.limitFramerate;
                renderDistance = mc.gameSettings.renderDistanceChunks;
                retrieved = true;
            }
        }
    }
    public static Config config = new Config();
    public EventListener getEventListener() {
        return eventListener;
    }
}