package dev.zaan.chunkreturn;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FrameStatsTest {
    @Test void averageAndReset() {
        FrameStats s = new FrameStats();
        s.add(10); s.add(30);
        assertEquals(20, s.averageMs(), .0001);
        s.reset();
        assertEquals(0, s.averageMs(), .0001);
    }
    @Test void boundedWindow() {
        FrameStats s = new FrameStats();
        for (int i = 0; i < 800; i++) s.add(20);
        assertEquals(50, s.onePercentLowFps(), .0001);
    }
}
