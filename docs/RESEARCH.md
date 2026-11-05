# Minecraft 26.3 renderer research

Minecraft 26.3 `LevelRenderer.compileSections(CameraRenderState)` invokes either `RenderSection.compileSync(RenderSectionRegion)` or `compileAsync(RenderSectionRegion)` depending on proximity and player changes. The dispatcher owns an async compile queue with `getCompileQueueSize()`.

This prototype redirects a bounded number of sync calls to vanilla async compiling only when previous render intervals exceed the frame threshold and the queue has spare capacity. **It does not skip processing or modify saved worlds.** It may cause some sections to appear later.

Source: https://mc-packet-reference.netlify.app/26.x/source/net/minecraft/client/renderer/levelrenderer/
Fabric 26.3 guidance: https://fabricmc.net/2026/09/15/263.html

Sodium may replace these internals. When Sodium is detected, ChunkReturn disables the experimental scheduler. Compatibility with other mods unverified. No benchmark results exist yet.

Risks: changed mixin target signatures can fail during startup; renderer intervals are not exact GPU frame time; frame spikes may originate outside section compilation. Real Minecraft launch and profiling are mandatory.
