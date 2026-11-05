package dev.zaan.chunkreturn.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;

public final class Config {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public boolean enabled = false; // Opt in, until proven stable
    public boolean autoMode = true;
    public String profile = "Balanced";
    public double frameThresholdMs = 24.0;
    public int maxRedirectsPerFrame = 3;
    public int maxVanillaQueueDepth = 64;
    public boolean overlay = false;

    public void applyPreset(String choice) {
        profile = choice;
        switch (choice) {
            case "Conservative" -> { frameThresholdMs = 34; maxRedirectsPerFrame = 1; maxVanillaQueueDepth = 32; }
            case "Balanced" -> { frameThresholdMs = 24; maxRedirectsPerFrame = 3; maxVanillaQueueDepth = 64; }
            case "Aggressive" -> { frameThresholdMs = 17; maxRedirectsPerFrame = 6; maxVanillaQueueDepth = 96; }
            default -> profile = "Custom";
        }
    }
    public void normalize() {
        if (profile == null || !java.util.List.of("Conservative", "Balanced", "Aggressive", "Custom").contains(profile))
            profile = "Balanced";
        if (!Double.isFinite(frameThresholdMs)) frameThresholdMs = 24;
        frameThresholdMs = Math.max(12, Math.min(100, frameThresholdMs));
        maxRedirectsPerFrame = Math.max(0, Math.min(16, maxRedirectsPerFrame));
        maxVanillaQueueDepth = Math.max(1, Math.min(256, maxVanillaQueueDepth));
    }
    private static Path path() {
        return FabricLoader.getInstance().getConfigDir().resolve("chunkreturn.json");
    }
    public static Config load() {
        try (Reader r = Files.newBufferedReader(path(), StandardCharsets.UTF_8)) {
            Config c = GSON.fromJson(r, Config.class);
            if (c != null) { c.normalize(); return c; }
        } catch (Exception ignored) { /* first run or bad JSON */ }
        return new Config();
    }
    public void save() {
        normalize();
        try {
            Path p = path();
            Files.createDirectories(p.getParent());
            try (Writer w = Files.newBufferedWriter(p, StandardCharsets.UTF_8)) {
                GSON.toJson(this, w);
            }
        } catch (Exception e) {
            System.err.println("[ChunkReturn] Could not save settings: " + e);
        }
    }
}
