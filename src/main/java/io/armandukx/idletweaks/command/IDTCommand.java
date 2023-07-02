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
                                                        Text message = Text.of(IdleTweaks.prefix + Formatting.YELLOW + "Background FPS Has Been Set to " + Formatting.RESET + IdleTweaks.getConfig().bFpsToggle);
                                                        MinecraftClient.getInstance().player.sendMessage(Text.Serializer.fromJson(Text.Serializer.toJson(message)), false);
                                                    }
                                                    return 1;
                                                })
                                )
                                .then(
                                        LiteralArgumentBuilder.<ServerCommandSource>literal("false")
                                                .executes((command) -> {
                                                    if (MinecraftClient.getInstance().player != null) {
                                                        IdleTweaks.getConfig().setbFpsToggle(false);
                                                        Text message = Text.of(IdleTweaks.prefix + Formatting.YELLOW + "Background FPS Has Been Set to " + Formatting.RESET + IdleTweaks.getConfig().bFpsToggle);
                                                        MinecraftClient.getInstance().player.sendMessage(Text.Serializer.fromJson(Text.Serializer.toJson(message)), false);
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
                                                        Text message = Text.of(IdleTweaks.prefix + Formatting.YELLOW + "Background Render Distance Has Been Set to " + Formatting.RESET + IdleTweaks.getConfig().bDistToggle);
                                                        MinecraftClient.getInstance().player.sendMessage(Text.Serializer.fromJson(Text.Serializer.toJson(message)), false);
                                                    }
                                                    return 1;
                                                })
                                )
                                .then(
                                        LiteralArgumentBuilder.<ServerCommandSource>literal("false")
                                                .executes((command) -> {
                                                    if (MinecraftClient.getInstance().player != null) {
                                                        IdleTweaks.getConfig().setbDistToggle(false);
                                                        Text message = Text.of(IdleTweaks.prefix + Formatting.YELLOW + "Background Render Distance Has Been Set to " + Formatting.RESET + IdleTweaks.getConfig().bDistToggle);
                                                        MinecraftClient.getInstance().player.sendMessage(Text.Serializer.fromJson(Text.Serializer.toJson(message)), false);
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
                                                        Text message = Text.of(IdleTweaks.prefix + Formatting.YELLOW + "Mute Background Sounds Has Been Set to " + Formatting.RESET + IdleTweaks.getConfig().bVolumeToggle);
                                                        MinecraftClient.getInstance().player.sendMessage(Text.Serializer.fromJson(Text.Serializer.toJson(message)), false);
                                                    }
                                                    return 1;
                                                })
                                )
                                .then(
                                        LiteralArgumentBuilder.<ServerCommandSource>literal("false")
                                                .executes((command) -> {
                                                    if (MinecraftClient.getInstance().player != null) {
                                                        IdleTweaks.getConfig().setbVolumeToggle(false);
                                                        Text message = Text.of(IdleTweaks.prefix + Formatting.YELLOW + "Mute Background Sounds Has Been Set to " + Formatting.RESET + IdleTweaks.getConfig().bVolumeToggle);
                                                        MinecraftClient.getInstance().player.sendMessage(Text.Serializer.fromJson(Text.Serializer.toJson(message)), false);
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
                                            if (inputNumber >= 0 && inputNumber <= 32) {
                                                IdleTweaks.getConfig().setBackgroundFps(IntegerArgumentType.getInteger(ctx, "number"));
                                            }
                                            else {
                                                if (MinecraftClient.getInstance().player != null){
                                                    Text message = Text.of(IdleTweaks.prefix + "Must be between 1 and 999");
                                                    MinecraftClient.getInstance().player.sendMessage(Text.Serializer.fromJson(Text.Serializer.toJson(message)), false);
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
                                                    Text message = Text.of(IdleTweaks.prefix + "Must be between 2 and 32");
                                                    MinecraftClient.getInstance().player.sendMessage(Text.Serializer.fromJson(Text.Serializer.toJson(message)), false);
                                                }
                                            }
                                            return 1;
                                        })
                                )
                );

        dispatcher.register(builder);
    }

    private static int helpResponse(ServerCommandSource source) {
        source.sendFeedback(Text.of(Formatting.YELLOW + "/idt Fps " + Formatting.RESET + "[true/false] - This feature allows you to enable or disable the 'Background FPS' setting\n----------------------------------------------"), false);
        source.sendFeedback(Text.of(Formatting.YELLOW + "/idt RenderDistance " + Formatting.RESET + "[true/false] - This feature allows you to enable or disable the 'Background Render Distance' setting\n----------------------------------------------"), false);
        source.sendFeedback(Text.of(Formatting.YELLOW + "/idt Sounds " + Formatting.RESET + "[true/false] - This setting mutes all sounds in Minecraft when the game is not in focus\n----------------------------------------------"), false);
        source.sendFeedback(Text.of(Formatting.YELLOW + "/idt BackgroundFps " + Formatting.RESET + "[number] - The limit for FPS when minecraft is in background\n----------------------------------------------"), false);
        source.sendFeedback(Text.of(Formatting.YELLOW + "/idt BackgroundRenderDistance " + Formatting.RESET + "[number] - The limit for Render Distance when minecraft is in background\n----------------------------------------------"), false);

        source.sendFeedback(Text.of(Formatting.GOLD + "SCROLL UP" + Formatting.RESET + "\n----------------------------------------------"), false);
        return 1;
    }
}