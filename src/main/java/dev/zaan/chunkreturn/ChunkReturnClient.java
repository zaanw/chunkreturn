package dev.zaan.chunkreturn;

import com.mojang.blaze3d.platform.InputConstants;
import dev.zaan.chunkreturn.config.Config;
import dev.zaan.chunkreturn.ui.ConfigScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public final class ChunkReturnClient implements ClientModInitializer {
    public static Config config;
    public static final FrameStats STATS = new FrameStats();
    public static long redirected;
    public static boolean sodiumPresent;

    @Override public void onInitializeClient() {
        config = Config.load();
        sodiumPresent = FabricLoader.getInstance().isModLoaded("sodium");
        KeyMapping.Category category = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath("chunkreturn", "settings"));
        KeyMapping key = KeyMappingHelper.registerKeyMapping(
            new KeyMapping("key.chunkreturn.open", InputConstants.Type.KEYSYM,
                InputConstants.KEY_RSHIFT, category));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (key.consumeClick()) client.setScreen(new ConfigScreen(client.screen));
        });
    }
    public static boolean active() { return config != null && config.enabled && !sodiumPresent; }
}
