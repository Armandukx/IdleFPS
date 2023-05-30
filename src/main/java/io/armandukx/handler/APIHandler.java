/*
 * ArmandukxSB - A customizable quality of life mod for Hypixel Skyblock
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

package io.armandukx.handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class APIHandler {
    public static JsonObject getResponse(String urlString, boolean hasError) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "ASB/1.0");

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                String input;
                StringBuilder response = new StringBuilder();

                while ((input = in.readLine()) != null) {
                    response.append(input);
                }
                in.close();

                Gson gson = new Gson();

                return gson.fromJson(response.toString(), JsonObject.class);
            } else {
                if (hasError) {
                    InputStream errorStream = conn.getErrorStream();
                    try (Scanner scanner = new Scanner(errorStream)) {
                        scanner.useDelimiter("\\Z");
                        String error = scanner.next();

                        Gson gson = new Gson();
                        return gson.fromJson(error, JsonObject.class);
                    }
                } else {
                    player.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_RED + "Request failed. HTTP Error Code: " + conn.getResponseCode()));
                }
            }
        } catch (IOException ex) {
            player.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_RED + "An error has occured. See logs for more details."));
            ex.printStackTrace();
        }
        return new JsonObject();
    }
}
