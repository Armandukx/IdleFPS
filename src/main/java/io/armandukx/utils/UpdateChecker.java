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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion;

public class UpdateChecker {

    static boolean updateChecked = false;

    @SubscribeEvent
    public void onJoin(EntityJoinWorldEvent event) {
        if (!updateChecked) {
            if (event.getEntity() instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) event.getEntity();
                updateChecked = true;

                new Thread(() -> {

                    System.out.println("Checking for updates...");
                    JsonObject latestRelease = APIHandler.getResponse("https://api.github.com/repos/Armandukx/IdleFPS/releases/latest", false);

                    String latestTag = latestRelease.get("tag_name").getAsString();
                    String McVersion = latestRelease.get("name").getAsString();
                    DefaultArtifactVersion currentVersion = new DefaultArtifactVersion(IdleTweaks.VERSION);
                    DefaultArtifactVersion latestVersion = new DefaultArtifactVersion(latestTag.substring(1));

                    if (McVersion.contains("1.12.2")) {
                        if (currentVersion.compareTo(latestVersion) < 0) {
                            String releaseURL = latestRelease.get("html_url").getAsString();

                            TextComponentString update = new TextComponentString(TextFormatting.GREEN + "" + TextFormatting.BOLD + "  [UPDATE]  ");
                            update.setStyle(update.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, releaseURL)));

                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                            player.sendMessage(new TextComponentString(TextFormatting.BOLD + IdleTweaks.prefix + TextFormatting.DARK_RED + IdleTweaks.NAME + " is outdated. Please update to " + latestTag + ".\n").appendSibling(update));
                        }
                    }
                }).start();
            }
        }
    }
}