package me.flashyreese.mods.sodiumextra.client;

import com.google.common.collect.EvictingQueue;
import me.flashyreese.mods.sodiumextra.mixin.gui.MinecraftClientAccessor;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import java.util.Comparator;
import java.util.Queue;

public class ClientTickHandler {
    private int averageFps, lowestFps, highestFps;
    private final Queue<Integer> fpsQueue = EvictingQueue.create(200);

    public void onClientInitialize() {
        ClientTickEvents.START_CLIENT_TICK.register(minecraftClient -> {
            int currentFPS = MinecraftClientAccessor.getCurrentFPS();
            this.fpsQueue.add(currentFPS);
            this.averageFps = (int) this.fpsQueue.stream().mapToInt(Integer::intValue).average().orElse(0);
            this.lowestFps = this.fpsQueue.stream().min(Comparator.comparingInt(e -> e)).orElse(0);
            this.highestFps = this.fpsQueue.stream().max(Comparator.comparingInt(e -> e)).orElse(0);
        });
    }

    public int getAverageFps() {
        return this.averageFps;
    }

    public int getLowestFps() {
        return this.lowestFps;
    }

    public int getHighestFps() {
        return this.highestFps;
    }
}
