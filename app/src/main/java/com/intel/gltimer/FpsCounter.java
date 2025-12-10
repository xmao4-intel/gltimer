package com.intel.gltimer;

public class FpsCounter {
    private final long[] frameTimes;
    private final int windowSize;
    private int currentIndex = 0;
    private int frameCount = 0;
    private long totalFrames = 0;
    
    public FpsCounter(int windowSize) {
        this.windowSize = windowSize;
        this.frameTimes = new long[windowSize];
    }
    
    /**
     * Call this every frame to record the timestamp
     */
    public void recordFrame() {
        frameTimes[currentIndex] = System.nanoTime();
        currentIndex = (currentIndex + 1) % windowSize;
        if (frameCount < windowSize) {
            frameCount++;
        }
        totalFrames++;
    }
    
    /**
     * Calculate FPS based on sliding window
     * @return current FPS, or 0 if not enough frames recorded
     */
    public float getFps() {
        if (frameCount < 2) {
            return 0f;
        }
        
        // Get oldest and newest frame times
        int oldestIndex = frameCount < windowSize ? 0 : currentIndex;
        int newestIndex = (currentIndex - 1 + windowSize) % windowSize;
        
        long duration = frameTimes[newestIndex] - frameTimes[oldestIndex];
        
        if (duration <= 0) {
            return 0f;
        }
        
        // Convert nanoseconds to seconds and calculate FPS
        return (frameCount - 1) / (duration / 1_000_000_000f);
    }
    
    /**
     * Get total frame count since start
     * @return total number of frames rendered
     */
    public long getTotalFrames() {
        return totalFrames;
    }
}
