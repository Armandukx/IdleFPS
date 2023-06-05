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

package io.armandukx.utils;

import com.google.gson.JsonObject;
import io.armandukx.IdleTweaks;
import io.armandukx.handler.APIHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion;

public class UpdateChecker {

    static boolean updateChecked = false;

    @SubscribeEvent
    public void onJoin(EntityJoinWorldEvent event) {
        if (!updateChecked) {
            updateChecked = true;

            new Thread(() -> {
                Minecraft mc = Minecraft.getMinecraft();
                System.out.println("Checking for updates...");
                JsonObject latestRelease = APIHandler.getResponse("https://api.modrinth.com/updates/Vnjlu1sC/forge_updates.json", false);

                System.out.println("Has promos?");
                if (latestRelease != null && latestRelease.has("promos")) {
                    JsonObject promos = latestRelease.getAsJsonObject("promos");
                    if (promos.has("1.8.9-recommended")) {
                        String recommendedVersion = promos.get("1.8.9-recommended").getAsString().substring(1);

                        DefaultArtifactVersion currentVersion = new DefaultArtifactVersion(IdleTweaks.VERSION);
                        DefaultArtifactVersion latestVersion = new DefaultArtifactVersion(recommendedVersion);

                        if (currentVersion.compareTo(latestVersion) < 0) {
                            String releaseURL = "https://modrinth.com/mod/Vnjlu1sC/versions?g=1.8.9";

                            ChatComponentText update = new ChatComponentText(EnumChatFormatting.GREEN + "" + EnumChatFormatting.BOLD + "  [UPDATE]  ");
                            update.setChatStyle(update.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, releaseURL)));
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                            mc.thePlayer.addChatMessage(new ChatComponentText( EnumChatFormatting.BOLD + IdleTweaks.prefix + EnumChatFormatting.DARK_RED + IdleTweaks.NAME+ " " + IdleTweaks.VERSION + " is outdated. Please update to " + latestVersion + ".\n").appendSibling(update));
                        }
                    }
                }
            }).start();
        }
    }
}