package io.armandukx.idletweaks.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.armandukx.idletweaks.IdleTweaks;
import io.armandukx.idletweaks.config.IdleTweaksConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class IDTCommand {
    public IDTCommand(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                LiteralArgumentBuilder.<CommandSource>literal("idt")
                        .then(
                                LiteralArgumentBuilder.<CommandSource>literal("Help")
                                        .executes((command) -> helpResponse(command.getSource()))
                        )
                        .then(
                                LiteralArgumentBuilder.<CommandSource>literal("Fps")
                                        .then(
                                                LiteralArgumentBuilder.<CommandSource>literal("true")
                                                        .executes((command) -> {
                                                            if (Minecraft.getInstance().player != null) {
                                                                IdleTweaksConfig.bFpsToggle.set(true);
                                                                IdleTweaksConfig.bFpsToggle.save();
                                                                Minecraft.getInstance().player.sendMessage(new StringTextComponent(IdleTweaks.prefix + TextFormatting.YELLOW + "Background FPS Has Been Set to " + TextFormatting.RESET + IdleTweaksConfig.bFpsToggle.get()), Minecraft.getInstance().player.getUUID());
                                                            }
                                                            return 1;
                                                        })
                                        )
                                        .then(
                                                LiteralArgumentBuilder.<CommandSource>literal("false")
                                                        .executes((command) -> {
                                                            if (Minecraft.getInstance().player != null) {
                                                                IdleTweaksConfig.bFpsToggle.set(false);
                                                                IdleTweaksConfig.bFpsToggle.save();
                                                                Minecraft.getInstance().player.sendMessage(new StringTextComponent(IdleTweaks.prefix + TextFormatting.YELLOW + "Background FPS Has Been Set to " + TextFormatting.RESET + IdleTweaksConfig.bFpsToggle.get()), Minecraft.getInstance().player.getUUID());
                                                            }
                                                            return 1;
                                                        })
                                        )
                        )
                        .then(
                                LiteralArgumentBuilder.<CommandSource>literal("RenderDistance")
                                        .then(
                                                LiteralArgumentBuilder.<CommandSource>literal("true")
                                                        .executes((command) -> {
                                                            if (Minecraft.getInstance().player != null) {
                                                                IdleTweaksConfig.bDistToggle.set(true);
                                                                IdleTweaksConfig.bDistToggle.save();
                                                                Minecraft.getInstance().player.sendMessage(new StringTextComponent(IdleTweaks.prefix + TextFormatting.YELLOW + "Background Render Distance Has Been Set to " + TextFormatting.RESET + IdleTweaksConfig.bDistToggle.get()), Minecraft.getInstance().player.getUUID());
                                                            }
                                                            return 1;
                                                        })
                                        )
                                        .then(
                                                LiteralArgumentBuilder.<CommandSource>literal("false")
                                                        .executes((command) -> {
                                                            if (Minecraft.getInstance().player != null) {
                                                                IdleTweaksConfig.bDistToggle.set(false);
                                                                IdleTweaksConfig.bDistToggle.save();
                                                                Minecraft.getInstance().player.sendMessage(new StringTextComponent(IdleTweaks.prefix + TextFormatting.YELLOW + "Background Render Distance Has Been Set to " + TextFormatting.RESET + IdleTweaksConfig.bDistToggle.get()), Minecraft.getInstance().player.getUUID());
                                                            }
                                                            return 1;
                                                        })
                                        )
                        )
                        .then(
                                LiteralArgumentBuilder.<CommandSource>literal("Sounds")
                                        .then(
                                                LiteralArgumentBuilder.<CommandSource>literal("true")
                                                        .executes((command) -> {
                                                            if (Minecraft.getInstance().player != null) {
                                                                IdleTweaksConfig.bVolumeToggle.set(true);
                                                                IdleTweaksConfig.bVolumeToggle.save();
                                                                Minecraft.getInstance().player.sendMessage(new StringTextComponent(IdleTweaks.prefix + TextFormatting.YELLOW + "Mute Background Sounds Has Been Set to " + TextFormatting.RESET + IdleTweaksConfig.bVolumeToggle.get()), Minecraft.getInstance().player.getUUID());
                                                            }
                                                            return 1;
                                                        })
                                        )
                                        .then(
                                                LiteralArgumentBuilder.<CommandSource>literal("false")
                                                        .executes((command) -> {
                                                            if (Minecraft.getInstance().player != null) {
                                                                IdleTweaksConfig.bVolumeToggle.set(false);
                                                                IdleTweaksConfig.bVolumeToggle.save();
                                                                Minecraft.getInstance().player.sendMessage(new StringTextComponent(IdleTweaks.prefix + TextFormatting.YELLOW + "Mute Background Sounds Has Been Set to " + TextFormatting.RESET + IdleTweaksConfig.bVolumeToggle.get()), Minecraft.getInstance().player.getUUID());
                                                            }
                                                            return 1;
                                                        })
                                        )
                        )
                        .then(
                                LiteralArgumentBuilder.<CommandSource>literal("BackgroundFps")
                                        .then(Commands.argument("[insert number]", IntegerArgumentType.integer())
                                                .executes(ctx -> {
                                                    int inputNumber = IntegerArgumentType.getInteger(ctx, "[insert number]");
                                                    if (inputNumber >= 1 && inputNumber <= 999) {
                                                        IdleTweaksConfig.backgroundFps.set(inputNumber);
                                                        IdleTweaksConfig.backgroundFps.save();
                                                    }
                                                    else {
                                                        if (Minecraft.getInstance().player != null){
                                                            Minecraft.getInstance().player.sendMessage(new StringTextComponent(IdleTweaks.prefix + "Must be between 1 and 999"), Minecraft.getInstance().player.getUUID());
                                                        }
                                                    }
                                                    return 1;
                                                })))
                        .then(
                                LiteralArgumentBuilder.<CommandSource>literal("BackgroundRenderDistance")
                                        .then(Commands.argument("[insert number]", IntegerArgumentType.integer())
                                                .executes(ctx -> {
                                                            int inputNumber = IntegerArgumentType.getInteger(ctx, "[insert number]");
                                                            if (inputNumber >= 0 && inputNumber <= 32) {
                                                                IdleTweaksConfig.backgroundRenderDist.set(inputNumber);
                                                                IdleTweaksConfig.backgroundRenderDist.save();
                                                            }
                                                            else {
                                                                if (Minecraft.getInstance().player != null){
                                                                    Minecraft.getInstance().player.sendMessage(new StringTextComponent(IdleTweaks.prefix + "Must be between 0 and 32"), Minecraft.getInstance().player.getUUID());
                                                                }
                                                            }
                                                            return 1;
                                                        }
                                                ))));
    }

    private int helpResponse(CommandSource source) {
        if (Minecraft.getInstance().player != null) {
            Minecraft.getInstance().player.sendMessage(new StringTextComponent(TextFormatting.YELLOW + "/idt Fps " + TextFormatting.RESET + "[true/false] - This feature allows you to enable or disable the 'Background FPS' setting\n----------------------------------------------"), Minecraft.getInstance().player.getUUID());
            Minecraft.getInstance().player.sendMessage(new StringTextComponent(TextFormatting.YELLOW + "/idt RenderDistance " + TextFormatting.RESET + "[true/false] - This feature allows you to enable or disable the 'Background Render Distance' setting\n----------------------------------------------"), Minecraft.getInstance().player.getUUID());
            Minecraft.getInstance().player.sendMessage(new StringTextComponent(TextFormatting.YELLOW + "/idt Sounds " + TextFormatting.RESET + "[true/false] - This setting mutes all sounds in Minecraft when the game is not in focus\n----------------------------------------------"), Minecraft.getInstance().player.getUUID());
            Minecraft.getInstance().player.sendMessage(new StringTextComponent(TextFormatting.YELLOW + "/idt BackgroundFps " + TextFormatting.RESET + "[number] - The limit for FPS when minecraft is in background\n----------------------------------------------"), Minecraft.getInstance().player.getUUID());
            Minecraft.getInstance().player.sendMessage(new StringTextComponent(TextFormatting.YELLOW + "/idt BackgroundRenderDistance " + TextFormatting.RESET + "[number] - The limit for Render Distance when minecraft is in background\n----------------------------------------------"), Minecraft.getInstance().player.getUUID());

            Minecraft.getInstance().player.sendMessage(new StringTextComponent(TextFormatting.GOLD + "SCROLL UP" + TextFormatting.RESET + "\n----------------------------------------------"), Minecraft.getInstance().player.getUUID());
        }
        return 1;
    }
}