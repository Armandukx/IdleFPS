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
    private long LastFrame = System.nanoTime();

    @Inject(method = "render", at = @At("HEAD"))
    private void LockFps(CallbackInfo ci) {
        if (!GameSettingsModifier.IdleActive) {
            LastFrame = System.nanoTime();
            return;
        }

        if (IdleTweaks.GetConfig().bFpsToggle) {
            long targetFrameNs = (long) (1_000_000_000.0 / IdleTweaks.GetConfig().backgroundFps);
            long now = System.nanoTime();
            long waitTime = targetFrameNs - (now - LastFrame);

            if (waitTime > 0) {
                try {
                    Thread.sleep(waitTime / 1_000_000, (int) (waitTime % 1_000_000));
                } catch (InterruptedException ignored) {
                }
            }

            LastFrame = System.nanoTime();
        }
    }
}
