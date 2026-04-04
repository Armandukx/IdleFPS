package io.armandukx.idletweaks.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import io.armandukx.idletweaks.IdleTweaks;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class IDTCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> builder = CommandManager.literal("idt")
                .then(CommandManager.literal("Help").executes(c -> helpResponse(c.getSource())))

                // FPS Toggle
                .then(CommandManager.literal("Fps")
                        .then(CommandManager.literal("true").executes(c -> setFpsToggle(c.getSource(),true)))
                        .then(CommandManager.literal("false").executes(c -> setFpsToggle(c.getSource(),false)))
                )

                // Render Distance Toggle
                .then(CommandManager.literal("RenderDistance")
                        .then(CommandManager.literal("true").executes(c -> setRenderDistanceToggle(c.getSource(),true)))
                        .then(CommandManager.literal("false").executes(c -> setRenderDistanceToggle(c.getSource(),false)))
                )

                // Sounds Toggle
                .then(CommandManager.literal("Sounds")
                        .then(CommandManager.literal("true").executes(c -> setSoundsToggle(c.getSource(),true)))
                        .then(CommandManager.literal("false").executes(c -> setSoundsToggle(c.getSource(),false)))
                )

                // Background FPS
                .then(CommandManager.literal("BackgroundFps")
                        .then(RequiredArgumentBuilder.<ServerCommandSource, Integer>argument("number", IntegerArgumentType.integer())
                                .executes(ctx -> {
                                    int val = IntegerArgumentType.getInteger(ctx, "number");
                                    if (val >= 1 && val <= 999) {
                                        IdleTweaks.GetConfig().setBackgroundFps(val);
                                    } else {
                                        sendPlayerMessage(ctx.getSource(),"Must be between 1 and 999");
                                    }
                                    return 1;
                                })
                        )
                )

                // Background Render Distance
                .then(CommandManager.literal("BackgroundRenderDistance")
                        .then(RequiredArgumentBuilder.<ServerCommandSource, Integer>argument("number", IntegerArgumentType.integer())
                                .executes(ctx -> {
                                    int val = IntegerArgumentType.getInteger(ctx, "number");
                                    if (val >= 2 && val <= 32) {
                                        IdleTweaks.GetConfig().setBackgroundRenderDist(val);
                                    } else {
                                        sendPlayerMessage(ctx.getSource(),"Must be between 2 and 32");
                                    }
                                    return 1;
                                })
                        )
                )

                // Cooldown
                .then(CommandManager.literal("Cooldown")
                        .then(RequiredArgumentBuilder.<ServerCommandSource, Integer>argument("number", IntegerArgumentType.integer())
                                .executes(ctx -> {
                                    int val = IntegerArgumentType.getInteger(ctx, "number");
                                    if (val >= 0) {
                                        IdleTweaks.GetConfig().setCooldown(val);
                                    } else {
                                        sendPlayerMessage(ctx.getSource(), "Minimum must be 0");
                                    }
                                    return 1;
                                })
                        )
                );

        dispatcher.register(builder);
    }

    // --- Helpers ---
    private static int setFpsToggle(ServerCommandSource source, boolean toggle) {
        IdleTweaks.GetConfig().setbFpsToggle(toggle);
        sendPlayerMessage(source, "Background FPS Has Been Set to " + toggle);
        return 1;
    }

    private static int setRenderDistanceToggle(ServerCommandSource source, boolean toggle) {
        IdleTweaks.GetConfig().setbDistToggle(toggle);
        sendPlayerMessage(source, "Background Render Distance Has Been Set to " + toggle);
        return 1;
    }

    private static int setSoundsToggle(ServerCommandSource source, boolean toggle) {
        IdleTweaks.GetConfig().setbVolumeToggle(toggle);
        sendPlayerMessage(source, "Mute Background Sounds Has Been Set to " + toggle);
        return 1;
    }

    private static void sendPlayerMessage(ServerCommandSource source, String msg) {
        source.sendMessage(Text.literal(IdleTweaks.prefix + Formatting.YELLOW + msg));
    }

    private static int helpResponse(ServerCommandSource source) {
        source.sendMessage(Text.of(Formatting.YELLOW + "/idt Fps " + Formatting.RESET + "[true/false] - Enable/disable Background FPS\n----------------------------------------------"));
        source.sendMessage(Text.of(Formatting.YELLOW + "/idt RenderDistance " + Formatting.RESET + "[true/false] - Enable/disable Background Render Distance\n----------------------------------------------"));
        source.sendMessage(Text.of(Formatting.YELLOW + "/idt Sounds " + Formatting.RESET + "[true/false] - Mute background sounds\n----------------------------------------------"));
        source.sendMessage(Text.of(Formatting.YELLOW + "/idt BackgroundFps " + Formatting.RESET + "[number] - Limit FPS in background\n----------------------------------------------"));
        source.sendMessage(Text.of(Formatting.YELLOW + "/idt BackgroundRenderDistance " + Formatting.RESET + "[number] - Limit Render Distance in background\n----------------------------------------------"));
        source.sendMessage(Text.of(Formatting.YELLOW + "/idt Cooldown " + Formatting.RESET + "[number] - Minimum wait time for the mod\n----------------------------------------------"));
        source.sendMessage(Text.of(Formatting.GOLD + "SCROLL UP" + Formatting.RESET + "\n----------------------------------------------"));
        return 1;
    }
}