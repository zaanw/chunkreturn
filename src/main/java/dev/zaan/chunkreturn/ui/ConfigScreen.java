package dev.zaan.chunkreturn.ui;

import dev.zaan.chunkreturn.ChunkReturnClient;
import dev.zaan.chunkreturn.config.Config;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class ConfigScreen extends Screen {
    private final Screen parent;
    private final Config c = ChunkReturnClient.config;

    public ConfigScreen(Screen parent) {
        super(Component.literal("ChunkReturn"));
        this.parent = parent;
    }
    @Override protected void init() {
        int x = width / 2 - 110, y = height / 2 - 89, w = 220;
        addRenderableWidget(Button.builder(Component.literal("Enabled: " + (c.enabled ? "ON" : "OFF")), b -> {
            c.enabled = !c.enabled; c.save(); rebuildWidgets();
        }).bounds(x,y,w,20).build());
        addRenderableWidget(Button.builder(Component.literal("Profile: " + c.profile), b -> {
            String next = switch (c.profile) {
                case "Conservative" -> "Balanced";
                case "Balanced" -> "Aggressive";
                default -> "Conservative";
            };
            c.applyPreset(next); c.save(); rebuildWidgets();
        }).bounds(x,y+25,w,20).build());
        addRenderableWidget(Button.builder(Component.literal("Auto mode: " + (c.autoMode ? "ON" : "OFF")), b -> {
            c.autoMode = !c.autoMode; c.save(); rebuildWidgets();
        }).bounds(x,y+50,w,20).build());
        addRenderableWidget(Button.builder(Component.literal("Threshold: " + (int)c.frameThresholdMs + " ms"), b -> {
            c.frameThresholdMs = c.frameThresholdMs >= 60 ? 12 : c.frameThresholdMs + 4;
            c.profile = "Custom"; c.save(); rebuildWidgets();
        }).bounds(x,y+75,w,20).build());
        addRenderableWidget(Button.builder(Component.literal("Queue limit: " + c.maxVanillaQueueDepth), b -> {
            c.maxVanillaQueueDepth = c.maxVanillaQueueDepth >= 128 ? 16 : c.maxVanillaQueueDepth + 16;
            c.profile = "Custom"; c.save(); rebuildWidgets();
        }).bounds(x,y+100,w,20).build());
        addRenderableWidget(Button.builder(Component.literal("Done"), b -> onClose())
          .bounds(x,y+125,w,20).build());
    }
    @Override public void render(GuiGraphics g, int mx, int my, float delta) {
        super.render(g, mx, my, delta);
        g.drawCenteredString(font, "ChunkReturn (experimental)", width/2, height/2-114, 0xB4E3FF);
        String status = ChunkReturnClient.sodiumPresent ? "Sodium: scheduler bypassed" : "No runtime tests yet";
        g.drawCenteredString(font, status, width/2, height/2+77, 0xAAAAAA);
    }
    @Override public void onClose() { minecraft.setScreen(parent); }
}
