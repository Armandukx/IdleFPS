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
            System.out.println("Checking for updates...");
            JsonArray releases = APIHandler.getArrayResponse("https://api.modrinth.com/v2/project/Vnjlu1sC/version");
            if (!releases.isEmpty()) {
                String versionNumber = releases.get(0).getAsJsonObject().get("version_number").getAsString();
                if (versionNumber.startsWith("v") || versionNumber.startsWith("V")) {
                    versionNumber = versionNumber.substring(1);
                }

                System.out.println("[IdleTweaks] Latest version string: " + versionNumber);

                int[] IDTParts = convertVersionStringToIntArray(IdleTweaks.VERSION);
                int[] versionNumberParts = convertVersionStringToIntArray(versionNumber);

                int IDTVersionInt = convertVersionPartsToInt(IDTParts);
                int versionNumberInt = convertVersionPartsToInt(versionNumberParts);

                System.out.println("[IdleTweaks] Installed version as int: " + IDTVersionInt);
                System.out.println("[IdleTweaks] Latest version as int: " + versionNumberInt);

                if (IDTVersionInt < versionNumberInt) {
                    System.out.println("[IdleTweaks] Update available!");
                    if (mc.player != null) {
                        // Schedule on main thread
                        String finalVersionNumber = versionNumber;
                        mc.execute(() -> {
                            mc.player.sendMessage(
                                    Text.literal(Formatting.BOLD + IdleTweaks.prefix + Formatting.DARK_RED +
                                            "Idle Tweaks " + IdleTweaks.VERSION + " is outdated. Please update to " + finalVersionNumber + ".\n"),
                                    false
                            );
                        });
                    }
                } else {
                    System.out.println("[IdleTweaks] You are on the latest version.");
                }
            } else {
                System.out.println("[IdleTweaks] No releases found.");
            }
        }
    }

    public static int[] convertVersionStringToIntArray(String version) {
        version = version.trim();
        if (version.startsWith("v") || version.startsWith("V")) {
            version = version.substring(1);
        }

        String[] parts = version.split("\\.");
        int[] intArray = new int[parts.length];

        for (int i = 0; i < parts.length; i++) {
            String p = parts[i].trim();
            if (p.isEmpty()) {
                intArray[i] = 0;
            } else {
                try {
                    intArray[i] = Integer.parseInt(p);
                } catch (NumberFormatException e) {
                    intArray[i] = 0;
                    System.err.println("[IdleTweaks] Warning: invalid version part '" + p + "' in version string: " + version);
                }
            }
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