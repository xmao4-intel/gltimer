package com.intel.gltimer;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimerRenderer implements GLSurfaceView.Renderer {
    private final Context context;
    private TextRenderer textRenderer;
    
    // FPS calculation using sliding window
    private FpsCounter fpsCounter;

    public TimerRenderer(Context context) {
        this.context = context;
        fpsCounter = new FpsCounter(100); // 100-frame sliding window
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        
        textRenderer = new TextRenderer(context);
        textRenderer.init();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // Clear screen
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        
        // Record frame for FPS calculation
        fpsCounter.recordFrame();
        float fps = fpsCounter.getFps();
        long frameNumber = fpsCounter.getTotalFrames();
        
        // Get current time with milliseconds
        long timeMillis = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.US);
        String timeString = sdf.format(new Date(timeMillis));
        int millis = (int) (timeMillis % 1000);
        String fullTimeString = String.format("%s.%03d", timeString, millis);
        
        // Format FPS with 2 decimal places
        String fpsString = String.format("FPS: %.2f", fps);
        
        // Format frame number
        String frameString = String.format("Frame: %d", frameNumber);
        
        // Draw time text (larger, at top)
        textRenderer.drawText(fullTimeString, -0.95f, 0.4f, 1.9f, 0.3f);
        
        // Draw FPS text (below time)
        textRenderer.drawText(fpsString, -0.95f, -0.1f, 1.5f, 0.3f);
        
        // Draw frame number (below FPS)
        textRenderer.drawText(frameString, -0.95f, -0.6f, 1.5f, 0.3f);
    }
}
