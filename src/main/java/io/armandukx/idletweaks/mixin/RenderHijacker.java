package io.armandukx.idletweaks.mixin;

import io.armandukx.idletweaks.IdleTweaks;
import io.armandukx.idletweaks.utils.GameSettingsModifier;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class RenderHijacker {
    @Unique
    private long lastFrame = System.nanoTime();

    @Inject(method = "render", at = @At("HEAD"))
    private void lockFps(CallbackInfo ci) {
        if (!GameSettingsModifier.idleActive) {
            lastFrame = System.nanoTime();
            return;
        }

        if (IdleTweaks.getConfig().bFpsToggle) {
            long targetFrameNs = (long) (1_000_000_000.0 / IdleTweaks.getConfig().backgroundFps);
            long now = System.nanoTime();
            long waitTime = targetFrameNs - (now - lastFrame);

            if (waitTime > 0) {
                try {
                    Thread.sleep(waitTime / 1_000_000, (int) (waitTime % 1_000_000));
                } catch (InterruptedException ignored) {
                }
            }

            lastFrame = System.nanoTime();
        }
    }
}
