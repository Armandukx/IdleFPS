package io.armandukx.idletweaks.utils;

import com.google.gson.JsonObject;
import io.armandukx.idletweaks.IdleTweaks;
import io.armandukx.idletweaks.handler.APIHandler;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class UpdateChecker {

    static boolean updateChecked = false;

    public static void init() {
        if (MinecraftClient.getInstance().world == null) return;
        ClientEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (!updateChecked) {
                updateChecked = true;

                new Thread(() -> {
                    MinecraftClient mc = MinecraftClient.getInstance();
                    System.out.println("Checking for updates...");
                    JsonObject latestRelease = APIHandler.getResponse("https://api.modrinth.com/updates/Vnjlu1sC/forge_updates.json", false);

                    System.out.println("Has promos?");
                    if (latestRelease != null && latestRelease.has("promos")) {
                        JsonObject promos = latestRelease.getAsJsonObject("promos");
                        if (promos.has("1.20.1-recommended")) {
                            String recommendedVersion = promos.get("1.20.1-recommended").getAsString().substring(1);

                            String currentVersion = IdleTweaks.VERSION;

                            if (currentVersion.compareTo(recommendedVersion) < 0) {
                                String releaseURL = "https://modrinth.com/mod/Vnjlu1sC/versions?g=1.20&g=1.20.1";

                                Text update = Text.of(Formatting.GREEN + "" + Formatting.BOLD + "  [UPDATE]  ");
                                update.getWithStyle(update.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, releaseURL)));
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException ex) {
                                    ex.printStackTrace();
                                }
                                if (mc.player != null){
                                    Text message = Text.of(Formatting.BOLD + IdleTweaks.prefix + Formatting.DARK_RED + IdleTweaks.NAME + " " + IdleTweaks.VERSION + " is outdated. Please update to " + recommendedVersion + ".\n").copy().append(update);
                                    mc.player.sendMessage(Text.Serializer.fromJson(Text.Serializer.toJson(message)), false);
                                }
                            }
                        }
                    }
                }).start();
            }
        });
    }
}