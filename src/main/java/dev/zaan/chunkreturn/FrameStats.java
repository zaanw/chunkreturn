package dev.zaan.chunkreturn;

import java.util.Arrays;

/** Measures intervals between LevelRenderer.render calls; not GPU frame time. */
public final class FrameStats {
    private final double[] samples = new double[600];
    private int size, next;
    private long lastStart;
    public void onRender() {
        long now = System.nanoTime();
        if (lastStart != 0) add((now - lastStart) / 1_000_000.0);
        lastStart = now;
    }
    public void reset() { size = next = 0; lastStart = 0; Arrays.fill(samples, 0); }
    public void add(double ms) {
        if (ms <= 0 || ms >= 1000) return;
        samples[next] = ms;
        next = (next + 1) % samples.length;
        size = Math.min(size + 1, samples.length);
    }
    public double latestMs() {
        return size == 0 ? 0 : samples[(next - 1 + samples.length) % samples.length];
    }
    public double averageMs() {
        if (size == 0) return 0;
        double sum = 0;
        for (int i = 0; i < size; i++) sum += samples[i];
        return sum / size;
    }
    public double onePercentLowFps() {
        if (size < 20) return 0;
        double[] copy = Arrays.copyOf(samples, size);
        Arrays.sort(copy);
        int tail = Math.max(1, (int)Math.ceil(size * 0.01));
        double sum = 0;
        for (int i = 0; i < tail; i++) sum += copy[size - 1 - i];
        return sum == 0 ? 0 : 1000 * tail / sum;
    }
}
