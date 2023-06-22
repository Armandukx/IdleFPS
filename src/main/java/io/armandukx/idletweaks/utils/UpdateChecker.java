package io.armandukx.idletweaks.utils;

import com.google.gson.JsonObject;
import io.armandukx.idletweaks.IdleTweaks;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import io.armandukx.idletweaks.handler.APIHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

@Mod.EventBusSubscriber(modid = IdleTweaks.MODID)
public class UpdateChecker {

    static boolean updateChecked = false;

    @SubscribeEvent
    public void onJoin(EntityJoinWorldEvent event) {
        if (!updateChecked) {
            updateChecked = true;

            new Thread(() -> {
                Minecraft mc = Minecraft.getInstance();
                System.out.println("Checking for updates...");
                JsonObject latestRelease = APIHandler.getResponse("https://api.modrinth.com/updates/Vnjlu1sC/forge_updates.json", false);

                System.out.println("Has promos?");
                if (latestRelease != null && latestRelease.has("promos")) {
                    JsonObject promos = latestRelease.getAsJsonObject("promos");
                    if (promos.has("1.16.5-recommended")) {
                        String recommendedVersion = promos.get("1.16.5-recommended").getAsString().substring(1);

                        DefaultArtifactVersion currentVersion = new DefaultArtifactVersion(IdleTweaks.VERSION);
                        DefaultArtifactVersion latestVersion = new DefaultArtifactVersion(recommendedVersion);

                        if (currentVersion.compareTo(latestVersion) < 0) {
                            String releaseURL = "https://modrinth.com/mod/Vnjlu1sC/versions?g=1.16.5";

                            StringTextComponent update = new StringTextComponent(TextFormatting.GREEN + "" + TextFormatting.BOLD + "  [UPDATE]  ");
                            update.setStyle(update.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, releaseURL)));
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                            if (mc.player != null){
                                mc.player.sendMessage(new StringTextComponent( TextFormatting.BOLD + IdleTweaks.prefix + TextFormatting.DARK_RED + IdleTweaks.NAME+ " " + IdleTweaks.VERSION + " is outdated. Please update to " + latestVersion + ".\n").append(update), Minecraft.getInstance().player.getUUID());
                            }
                        }
                    }
                }
            }).start();
        }
    }
}