package io.armandukx.idletweaks.utils;

import com.google.gson.JsonArray;
import io.armandukx.idletweaks.IdleTweaks;
import io.armandukx.idletweaks.handler.APIHandler;
import net.minecraft.client.MinecraftClient;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class UpdateChecker {
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    public static void check() {
        if (mc.world != null) {
            IdleTweaks._STOPCHECKING = true;
            new Thread(() -> {
                System.out.println("Checking for updates...");
                JsonArray releases = APIHandler.getArrayResponse("https://api.modrinth.com/v2/project/Vnjlu1sC/version");
                if (releases.size() > 0) {
                    String versionNumber = releases.get(0).getAsJsonObject().get("version_number").getAsString().substring(1);
                    int[] IDTParts = convertVersionStringToIntArray(IdleTweaks.VERSION);
                    int[] versionNumberParts = convertVersionStringToIntArray(versionNumber);
                    int IDTVersionInt = convertVersionPartsToInt(IDTParts);
                    int versionNumberInt = convertVersionPartsToInt(versionNumberParts);
                    System.out.println(versionNumberInt+IDTVersionInt);
                    if (IDTVersionInt < versionNumberInt)
                    {
                        if (MinecraftClient.getInstance().player != null) {
                            MinecraftClient.getInstance().player.sendMessage(Text.literal(Formatting.BOLD + IdleTweaks.prefix + Formatting.DARK_RED + "Idle Tweaks " + IdleTweaks.VERSION + " is outdated. Please update to " + versionNumber + ".\n"));
                        }
                    }
                } else {
                    System.out.println("No releases found.");
                }
            }).start();
        }
    }

    public static int[] convertVersionStringToIntArray(String version) {
        String[] parts = version.split("\\.");
        int[] intArray = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            intArray[i] = Integer.parseInt(parts[i]);
        }
        return intArray;
    }

    public static int convertVersionPartsToInt(int[] parts) {
        int result = 0;
        for (int i = 0; i < parts.length; i++) {
            result += parts[i] * Math.pow(10, (parts.length - i - 1) * 2);
        }
        return result;
    }
}