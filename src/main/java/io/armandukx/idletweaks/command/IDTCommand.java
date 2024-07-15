package io.armandukx.idletweaks.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import io.armandukx.idletweaks.IdleTweaks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;


import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class IDTCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> builder = CommandManager.literal("idt")
                .then(
                        LiteralArgumentBuilder.<ServerCommandSource>literal("Help")
                                .executes((command) -> helpResponse(command.getSource()))
                )
                .then(
                        LiteralArgumentBuilder.<ServerCommandSource>literal("Fps")
                                .then(
                                        LiteralArgumentBuilder.<ServerCommandSource>literal("true")
                                                .executes((command) -> {
                                                    if (MinecraftClient.getInstance().player != null) {
                                                        IdleTweaks.getConfig().setbFpsToggle(true);
                                                        MinecraftClient.getInstance().player.sendMessage(Text.literal(IdleTweaks.prefix + Formatting.YELLOW + "Background FPS Has Been Set to " + Formatting.RESET + IdleTweaks.getConfig().bFpsToggle));
                                                    }
                                                    return 1;
                                                })
                                )
                                .then(
                                        LiteralArgumentBuilder.<ServerCommandSource>literal("false")
                                                .executes((command) -> {
                                                    if (MinecraftClient.getInstance().player != null) {
                                                        IdleTweaks.getConfig().setbFpsToggle(false);
                                                        MinecraftClient.getInstance().player.sendMessage(Text.literal(IdleTweaks.prefix + Formatting.YELLOW + "Background FPS Has Been Set to " + Formatting.RESET + IdleTweaks.getConfig().bFpsToggle));
                                                    }
                                                    return 1;
                                                })
                                )
                )
                .then(
                        LiteralArgumentBuilder.<ServerCommandSource>literal("RenderDistance")
                                .then(
                                        LiteralArgumentBuilder.<ServerCommandSource>literal("true")
                                                .executes((command) -> {
                                                    if (MinecraftClient.getInstance().player != null) {
                                                        IdleTweaks.getConfig().setbDistToggle(true);
                                                        MinecraftClient.getInstance().player.sendMessage(Text.literal(IdleTweaks.prefix + Formatting.YELLOW + "Background Render Distance Has Been Set to " + Formatting.RESET + IdleTweaks.getConfig().bDistToggle));
                                                    }
                                                    return 1;
                                                })
                                )
                                .then(
                                        LiteralArgumentBuilder.<ServerCommandSource>literal("false")
                                                .executes((command) -> {
                                                    if (MinecraftClient.getInstance().player != null) {
                                                        IdleTweaks.getConfig().setbDistToggle(false);
                                                        MinecraftClient.getInstance().player.sendMessage(Text.literal(IdleTweaks.prefix + Formatting.YELLOW + "Background Render Distance Has Been Set to " + Formatting.RESET + IdleTweaks.getConfig().bDistToggle));
                                                    }
                                                    return 1;
                                                })
                                )
                )
                .then(
                        LiteralArgumentBuilder.<ServerCommandSource>literal("Sounds")
                                .then(
                                        LiteralArgumentBuilder.<ServerCommandSource>literal("true")
                                                .executes((command) -> {
                                                    if (MinecraftClient.getInstance().player != null) {
                                                        IdleTweaks.getConfig().setbVolumeToggle(true);
                                                        MinecraftClient.getInstance().player.sendMessage(Text.literal(IdleTweaks.prefix + Formatting.YELLOW + "Mute Background Sounds Has Been Set to " + Formatting.RESET + IdleTweaks.getConfig().bVolumeToggle));
                                                    }
                                                    return 1;
                                                })
                                )
                                .then(
                                        LiteralArgumentBuilder.<ServerCommandSource>literal("false")
                                                .executes((command) -> {
                                                    if (MinecraftClient.getInstance().player != null) {
                                                        IdleTweaks.getConfig().setbVolumeToggle(false);
                                                        MinecraftClient.getInstance().player.sendMessage(Text.literal(IdleTweaks.prefix + Formatting.YELLOW + "Mute Background Sounds Has Been Set to " + Formatting.RESET + IdleTweaks.getConfig().bVolumeToggle));
                                                    }
                                                    return 1;
                                                })
                                )
                )
                .then(
                        LiteralArgumentBuilder.<ServerCommandSource>literal("BackgroundFps")
                                .then(RequiredArgumentBuilder.<ServerCommandSource, Integer>argument("number", IntegerArgumentType.integer())
                                        .executes(ctx -> {
                                            int inputNumber = IntegerArgumentType.getInteger(ctx, "number");
                                            if (inputNumber >= 0 && inputNumber <= 999) {
                                                IdleTweaks.getConfig().setBackgroundFps(IntegerArgumentType.getInteger(ctx, "number"));
                                            }
                                            else {
                                                if (MinecraftClient.getInstance().player != null){
                                                    MinecraftClient.getInstance().player.sendMessage(Text.literal(IdleTweaks.prefix + "Must be between 1 and 999"));
                                                }
                                            }
                                            return 1;
                                        })
                                )
                )
                .then(
                        LiteralArgumentBuilder.<ServerCommandSource>literal("BackgroundRenderDistance")
                                .then(RequiredArgumentBuilder.<ServerCommandSource, Integer>argument("number", IntegerArgumentType.integer())
                                        .executes(ctx -> {
                                            int inputNumber = IntegerArgumentType.getInteger(ctx, "number");
                                            if (inputNumber >= 2 && inputNumber <= 32) {
                                                IdleTweaks.getConfig().setBackgroundRenderDist(IntegerArgumentType.getInteger(ctx, "number"));
                                            }
                                            else {
                                                if (MinecraftClient.getInstance().player != null){
                                                    MinecraftClient.getInstance().player.sendMessage(Text.literal(IdleTweaks.prefix + "Must be between 2 and 32"));
                                                }
                                            }
                                            return 1;
                                        })
                                )
                )
                                .then(
                                        LiteralArgumentBuilder.<ServerCommandSource>literal("Cooldown")
                                                .then(RequiredArgumentBuilder.<ServerCommandSource, Integer>argument("number", IntegerArgumentType.integer())
                                                        .executes(ctx -> {
                                                            int inputNumber = IntegerArgumentType.getInteger(ctx, "number");
                                                            if (inputNumber >= 0) {
                                                                IdleTweaks.getConfig().setCooldown(IntegerArgumentType.getInteger(ctx, "number"));
                                                            }
                                                            else {
                                                                if (MinecraftClient.getInstance().player != null){
                                                                    MinecraftClient.getInstance().player.sendMessage(Text.literal(IdleTweaks.prefix + "Minimum must be 0"));
                                                                }
                                                            }
                                                            return 1;
                                                        })
                                                )
                );

        dispatcher.register(builder);
    }

    private static int helpResponse(ServerCommandSource source) {
        source.sendMessage(Text.of(Formatting.YELLOW + "/idt Fps " + Formatting.RESET + "[true/false] - This feature allows you to enable or disable the 'Background FPS' setting\n----------------------------------------------"));
        source.sendMessage(Text.of(Formatting.YELLOW + "/idt RenderDistance " + Formatting.RESET + "[true/false] - This feature allows you to enable or disable the 'Background Render Distance' setting\n----------------------------------------------"));
        source.sendMessage(Text.of(Formatting.YELLOW + "/idt Sounds " + Formatting.RESET + "[true/false] - This setting mutes all sounds in Minecraft when the game is not in focus\n----------------------------------------------"));
        source.sendMessage(Text.of(Formatting.YELLOW + "/idt BackgroundFps " + Formatting.RESET + "[number] - The limit for FPS when minecraft is in background\n----------------------------------------------"));
        source.sendMessage(Text.of(Formatting.YELLOW + "/idt BackgroundRenderDistance " + Formatting.RESET + "[number] - The limit for Render Distance when minecraft is in background\n----------------------------------------------"));
        source.sendMessage(Text.of(Formatting.YELLOW + "/idt Cooldown" + Formatting.RESET + "[number] - The mod doesn't do anything until an amount of time\n----------------------------------------------"));

        source.sendMessage(Text.of(Formatting.GOLD + "SCROLL UP" + Formatting.RESET + "\n----------------------------------------------"));
        return 1;
    }
}