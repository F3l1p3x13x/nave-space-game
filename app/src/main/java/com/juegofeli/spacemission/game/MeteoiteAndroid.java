package com.juegofeli.spacemission.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import java.util.Random;

public class MeteoiteAndroid {
    private float x, y;
    private float velocityX, velocityY;
    private int width, height;
    private int color;
    private long creationTime;
    private static final int LIFETIME = 4000; // 4 seconds
    private Random random;
    private float rotation;
    private float rotationSpeed;
    private Paint paint;
    
    public MeteoiteAndroid(int screenWidth, int screenHeight) {
        this.random = new Random();
        this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.creationTime = System.currentTimeMillis();
        
        // Random size
        this.width = 15 + random.nextInt(20);
        this.height = 15 + random.nextInt(20);
        
        // Start from top of screen
        this.x = random.nextInt(screenWidth + 200) - 100;
        this.y = -height - random.nextInt(100);
        
        // Diagonal movement
        this.velocityX = -2 - random.nextFloat() * 4; // -2 to -6
        this.velocityY = 4 + random.nextFloat() * 6;  // 4 to 10
        
        // Fire colors
        int[] meteoriteColors = {
            Color.rgb(255, 69, 0),    // Red-orange
            Color.rgb(255, 140, 0),   // Dark orange
            Color.rgb(255, 165, 0),   // Orange
            Color.rgb(220, 20, 60),   // Crimson
            Color.rgb(255, 215, 0)    // Gold
        };
        this.color = meteoriteColors[random.nextInt(meteoriteColors.length)];
        
        // Rotation
        this.rotation = 0;
        this.rotationSpeed = random.nextFloat() * 20 - 10; // -10 to 10 degrees per frame
    }
    
    public void update() {
        x += velocityX;
        y += velocityY;
        
        rotation += rotationSpeed;
        if (rotation > 360) rotation -= 360;
        if (rotation < 0) rotation += 360;
    }
    
    public void draw(Canvas canvas) {
        canvas.save();
        
        // Rotate around center
        canvas.rotate(rotation, x + width / 2f, y + height / 2f);
        
        // Draw fire trail first (behind meteorite)
        drawFireTrail(canvas);
        
        // Draw main meteorite
        paint.setColor(color);
        canvas.drawCircle(x + width / 2f, y + height / 2f, width / 2f, paint);
        
        // Add bright center
        paint.setColor(Color.WHITE);
        paint.setAlpha(150);
        canvas.drawCircle(x + width / 2f, y + height / 2f, width / 4f, paint);
        paint.setAlpha(255);
        
        canvas.restore();
    }
    
    private void drawFireTrail(Canvas canvas) {
        // Simple fire trail effect
        for (int i = 1; i <= 4; i++) {
            float trailX = x - velocityX * i * 2;
            float trailY = y - velocityY * i * 2;
            float trailSize = width * (1.0f - i * 0.2f);
            
            if (trailSize > 2) {
                paint.setColor(color);
                paint.setAlpha((int)(255 * (1.0f - i * 0.25f)));
                canvas.drawCircle(trailX + width / 2f, trailY + height / 2f, trailSize / 2f, paint);
            }
        }
        paint.setAlpha(255);
    }
    
    public boolean isExpired() {
        return System.currentTimeMillis() - creationTime >= LIFETIME;
    }
    
    public boolean isOffScreen(int screenWidth, int screenHeight) {
        return x < -width - 100 || x > screenWidth + 100 || y > screenHeight + 100;
    }
    
    public Rect getBounds() {
        return new Rect((int)x, (int)y, (int)x + width, (int)y + height);
    }
    
    public Rect getCollisionBounds() {
        // Reduced collision area (70% of original)
        int collisionWidth = (int)(width * 0.7);
        int collisionHeight = (int)(height * 0.7);
        int offsetX = (width - collisionWidth) / 2;
        int offsetY = (height - collisionHeight) / 2;
        
        return new Rect((int)x + offsetX, (int)y + offsetY, 
                       (int)x + offsetX + collisionWidth, (int)y + offsetY + collisionHeight);
    }
    
    public float getX() { return x; }
    public float getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
} 