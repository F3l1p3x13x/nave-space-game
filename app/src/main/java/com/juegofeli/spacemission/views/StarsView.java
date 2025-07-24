package com.juegofeli.spacemission.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StarsView extends View {
    
    private static final int STAR_COUNT = 100;
    private List<Star> stars;
    private Paint starPaint;
    private Random random;
    private boolean isRunning = false;
    private Thread animationThread;
    
    public StarsView(Context context) {
        super(context);
        init();
    }
    
    public StarsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    public StarsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    
    private void init() {
        random = new Random();
        stars = new ArrayList<>();
        starPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        
        setBackgroundColor(Color.BLACK);
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        createStars(w, h);
    }
    
    private void createStars(int width, int height) {
        stars.clear();
        for (int i = 0; i < STAR_COUNT; i++) {
            stars.add(new Star(width, height));
        }
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        if (stars == null) return;
        
        // Dibujar fondo espacial con gradiente
        drawSpaceBackground(canvas);
        
        // Dibujar estrellas
        for (Star star : stars) {
            star.draw(canvas, starPaint);
        }
    }
    
    private void drawSpaceBackground(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        
        // Crear gradiente espacial
        RadialGradient spaceGradient = new RadialGradient(
            width / 2f, height / 2f, Math.max(width, height) / 2f,
            new int[]{0xFF001122, 0xFF000011, 0xFF000000},
            new float[]{0.0f, 0.7f, 1.0f},
            Shader.TileMode.CLAMP
        );
        
        starPaint.setShader(spaceGradient);
        canvas.drawRect(0, 0, width, height, starPaint);
        starPaint.setShader(null);
    }
    
    public void resume() {
        isRunning = true;
        animationThread = new Thread(this::runAnimation);
        animationThread.start();
    }
    
    public void pause() {
        isRunning = false;
        if (animationThread != null) {
            try {
                animationThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    private void runAnimation() {
        while (isRunning) {
            // Actualizar estrellas
            for (Star star : stars) {
                star.update(getWidth(), getHeight());
            }
            
            // Redibujar en el hilo principal
            post(this::invalidate);
            
            try {
                Thread.sleep(16); // ~60 FPS
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    
    private class Star {
        private float x, y;
        private float speed;
        private int size;
        private float brightness;
        private float depth;
        private boolean isTwinkling;
        private float twinklePhase;
        private int color;
        
        public Star(int screenWidth, int screenHeight) {
            reset(screenWidth, screenHeight);
        }
        
        private void reset(int screenWidth, int screenHeight) {
            this.x = random.nextInt(screenWidth);
            this.y = random.nextInt(screenHeight);
            this.depth = random.nextFloat();
            this.speed = 0.5f + depth * 2.5f;
            this.size = (int)(1 + depth * 4);
            this.brightness = 0.3f + depth * 0.7f;
            this.isTwinkling = random.nextFloat() < 0.3f;
            this.twinklePhase = random.nextFloat() * 6.28f;
            
            // Color basado en profundidad
            if (depth > 0.7f) {
                color = Color.WHITE;
            } else if (depth > 0.3f) {
                color = Color.CYAN;
            } else {
                color = Color.BLUE;
            }
        }
        
        public void update(int screenWidth, int screenHeight) {
            x -= speed;
            
            if (x < -size) {
                x = screenWidth + size;
                y = random.nextInt(screenHeight);
            }
            
            if (isTwinkling) {
                twinklePhase += 0.15f;
                if (twinklePhase > 6.28f) twinklePhase -= 6.28f;
            }
        }
        
        public void draw(Canvas canvas, Paint paint) {
            float currentBrightness = brightness;
            if (isTwinkling) {
                currentBrightness *= (0.5f + 0.5f * (float)Math.sin(twinklePhase));
            }
            
            int alpha = (int)(currentBrightness * 255);
            int starColor = Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
            
            paint.setColor(starColor);
            
            if (size <= 1) {
                canvas.drawPoint(x, y, paint);
            } else if (size <= 2) {
                canvas.drawCircle(x, y, size / 2f, paint);
            } else {
                // Estrella grande con halo
                paint.setAlpha((int)(alpha * 0.3f));
                canvas.drawCircle(x, y, size + 2, paint);
                paint.setAlpha(alpha);
                canvas.drawCircle(x, y, size / 2f, paint);
            }
        }
    }
} 