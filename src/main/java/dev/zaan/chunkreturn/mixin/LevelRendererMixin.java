package dev.zaan.chunkreturn.mixin;

import dev.zaan.chunkreturn.ChunkReturnClient;
import dev.zaan.chunkreturn.config.Config;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.chunk.RenderSectionRegion;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
    @Shadow private SectionRenderDispatcher sectionRenderDispatcher;
    private int chunkreturn$redirectsThisFrame;

    @Inject(method = "render", at = @At("HEAD"))
    private void chunkreturn$sample(CallbackInfo ci) {
        ChunkReturnClient.STATS.onRender();
    }
    @Inject(method = "compileSections", at = @At("HEAD"))
    private void chunkreturn$reset(CallbackInfo ci) {
        chunkreturn$redirectsThisFrame = 0;
    }
    @Redirect(method = "compileSections", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/client/renderer/chunk/SectionRenderDispatcher$RenderSection;compileSync(Lnet/minecraft/client/renderer/chunk/RenderSectionRegion;)V"))
    private void chunkreturn$adaptiveCompile(SectionRenderDispatcher.RenderSection section, RenderSectionRegion region) {
        Config c = ChunkReturnClient.config;
        if (ChunkReturnClient.active() && c.autoMode
                && ChunkReturnClient.STATS.latestMs() > c.frameThresholdMs
                && chunkreturn$redirectsThisFrame < c.maxRedirectsPerFrame
                && sectionRenderDispatcher != null
                && sectionRenderDispatcher.getCompileQueueSize() < c.maxVanillaQueueDepth) {
            section.compileAsync(region);
            chunkreturn$redirectsThisFrame++;
            ChunkReturnClient.redirected++;
        } else {
            section.compileSync(region);
        }
    }
}
