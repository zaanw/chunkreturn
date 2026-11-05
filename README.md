# ChunkReturn (experimental)

Fabric client mod for Minecraft **26.3**, by **zaan**.

Tries to reduce long render-thread stalls when returning to areas with chunk-section rebuilds. Under poor frame cadence, redirects a bounded number of synchronous section mesh compilation requests to Minecraft's own asynchronous compiler, with backpressure. **Performance improvement is unverified.** Nearby section updates may appear later.

### Controls
- **Right Shift:** open in-game config
- Config saved to `config/chunkreturn.json`
- Opt-in: optimization is **disabled by default** until in-game testing
- Conservative / Balanced / Aggressive profiles; threshold and queue limit
- Automatically bypasses experimental redirection if Sodium is loaded

### Build
Requires JDK 25 and Gradle 9.6. Run `gradle clean build` or download the JAR from a **successful** GitHub Actions run. The Gradle wrapper is not bundled yet.

### Prism Launcher
Create Minecraft 26.3 Fabric instance, add matching Fabric API, then add the built mod JAR to its Mods tab. Back up your world before testing experimental mods.

### Benchmarks and safety
See [research](docs/RESEARCH.md) and [benchmark protocol](docs/BENCHMARKS.md). No measured speedup claims; Sodium and third-party renderer compatibility are unverified.

MIT License. Not yet released on Modrinth.
