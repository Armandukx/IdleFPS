package io.armandukx.idletweaks;

import io.armandukx.idletweaks.config.IdleTweaksConfig;
import io.armandukx.idletweaks.utils.UpdateChecker;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(IdleTweaks.MODID)
public class IdleTweaks {
    public static final String NAME = "IdleTweaks";
    public static final String MODID = "idletweaks";
    public static final String VERSION = "1.0.6";
    public static final String prefix =
            TextFormatting.YELLOW + "[I" + TextFormatting.GREEN + "D" + TextFormatting.RED + "T] " + TextFormatting.RESET;
    private boolean retrieved = false;
    private boolean closing = false;
    public static int renderDistance = 0;
    public static int fps = 10;
    UpdateChecker updateChecker = new UpdateChecker();
    IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
    GameSettings gameSettings = Minecraft.getInstance().options;

    public IdleTweaks() {
        eventBus.addListener(this::setup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, IdleTweaksConfig.SPEC, "idletweaks.toml");
    }

    private void setup(final FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(updateChecker);
        if (!retrieved) {
            fps = Minecraft.getInstance().options.framerateLimit;
            renderDistance = Minecraft.getInstance().options.renderDistance;
            retrieved = true; // Just being careful
            System.out.println(IdleTweaks.prefix + "Current Fps: " + fps + " Current Render Distance: " + renderDistance);
        }
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerStopping(FMLServerStoppingEvent event) {
        System.out.println("Minecraft is closing");

        if (IdleTweaksConfig.bFpsToggle.get()) {
            setFpsLimit(fps);
            gameSettings.save();
            System.out.println(gameSettings.framerateLimit);
        }
        if (IdleTweaksConfig.bDistToggle.get()) {
            gameSettings.renderDistance = IdleTweaks.renderDistance;
            gameSettings.save();
            System.out.println(gameSettings.renderDistance);
        }
        if (IdleTweaksConfig.bVolumeToggle.get()) {
            if (gameSettings.getSoundSourceVolume(SoundCategory.MASTER) <= 0) {
                Minecraft.getInstance().getSoundManager().resume();
            }
        }
        closing = true;
    }

    @SubscribeEvent
    public void onUpdateDisplay(TickEvent.RenderTickEvent event) {
        if (closing) return;

        if (event.phase != TickEvent.Phase.START) return;

        if (!Minecraft.getInstance().isWindowActive()) {
            if (IdleTweaksConfig.bFpsToggle.get()){
                setFpsLimit(IdleTweaksConfig.backgroundFps.get());
            }
            if (IdleTweaksConfig.bDistToggle.get()) {
                gameSettings.renderDistance = IdleTweaksConfig.backgroundRenderDist.get();
            }
            if (IdleTweaksConfig.bVolumeToggle.get()) {
                Minecraft.getInstance().getSoundManager().stop();
            }
        } else {
            if (Minecraft.getInstance().screen != null && Minecraft.getInstance().screen.getClass().getName().equals("net.minecraft.client.gui.screen.VideoSettingsScreen")) {
                System.out.println("In Settings"); // For Debugging (I know it's not ideal)
            } else {
                if (IdleTweaksConfig.bFpsToggle.get()){
                    setFpsLimit(fps);
                }
                if (IdleTweaksConfig.bDistToggle.get()) {
                    gameSettings.renderDistance = IdleTweaks.renderDistance;
                }
            }
            if (IdleTweaksConfig.bVolumeToggle.get()) {
                if (gameSettings.getSoundSourceVolume(SoundCategory.MASTER) <= 0) {
                    Minecraft.getInstance().getSoundManager().resume();
                }
            }
        }
    }

    @SubscribeEvent
    public void onRunTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        if (!Minecraft.getInstance().isWindowActive()) return;
        int currentDist = gameSettings.renderDistance;
        int currentFps = gameSettings.framerateLimit;

        if (currentDist != IdleTweaks.renderDistance && (currentDist >= IdleTweaksConfig.backgroundRenderDist.get() + 1) || (currentDist <= IdleTweaksConfig.backgroundRenderDist.get() - 1)) {
            IdleTweaks.renderDistance = currentDist;
            gameSettings.save();
        }

        if (currentFps != IdleTweaks.fps && (currentFps >= IdleTweaksConfig.backgroundFps.get() + 1) || (currentFps <= IdleTweaksConfig.backgroundFps.get() - 1)) {
            IdleTweaks.fps = currentFps;
            gameSettings.save();
        }
    }

    public static void setFpsLimit(int fps) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getWindow().setFramerateLimit(fps);
    }
}
