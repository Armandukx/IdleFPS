package io.armandukx.idletweaks.config;

import io.armandukx.idletweaks.IdleTweaks;
import net.minecraftforge.common.ForgeConfigSpec;

public class IdleTweaksConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> backgroundRenderDist;
    public static final ForgeConfigSpec.ConfigValue<Integer> backgroundFps;
    public static final ForgeConfigSpec.ConfigValue<Boolean> bFpsToggle;
    public static final ForgeConfigSpec.ConfigValue<Boolean> bDistToggle;
    public static final ForgeConfigSpec.ConfigValue<Boolean> bVolumeToggle;

    static {
        BUILDER.push("Config file for "+ IdleTweaks.NAME+"! (DO NOT TOUCH)");

        bFpsToggle = BUILDER.comment("This feature allows you to enable or disable the 'Background Fps' setting").define("Background Fps Control", true);
        bDistToggle = BUILDER.comment("This feature allows you to enable or disable the 'Background Render Distance' setting").define("Background Render Distance Control", false);
        bVolumeToggle = BUILDER.comment("This setting mutes all sounds in Minecraft when the game is not in focus").define("Mute Sounds", true);

        backgroundFps = BUILDER.comment("The limit for Fps when minecraft is in background").define("Background Fps", 10);
        backgroundRenderDist = BUILDER.comment("The limit for Render Distance when minecraft is in background").define("Background Render Distance", 1);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
