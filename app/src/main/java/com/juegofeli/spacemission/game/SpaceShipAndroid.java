package com.juegofeli.spacemission.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class SpaceShipAndroid {
    private float x, y;
    private int width, height;
    private float speed;
    private int screenWidth, screenHeight;
    private Bitmap spaceShipBitmap;
    private Paint paint;
    
    // Target position for smooth movement
    private float targetX, targetY;
    private static final float MOVEMENT_SPEED = 8.0f;
    
    public SpaceShipAndroid(int screenWidth, int screenHeight, Bitmap bitmap) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.spaceShipBitmap = bitmap;
        this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        
        // Size adapted for mobile
        this.width = 100;
        this.height = 75;
        this.speed = MOVEMENT_SPEED;
        
        resetPosition();
    }
    
    public void resetPosition() {
        this.x = 100; // Start from left side
        this.y = screenHeight / 2f - height / 2f; // Center vertically
        this.targetX = x;
        this.targetY = y;
    }
    
    public void moveTowards(float touchX, float touchY) {
        targetX = touchX - width / 2f;
        targetY = touchY - height / 2f;
        
        // Clamp to screen bounds
        targetX = Math.max(0, Math.min(targetX, screenWidth - width));
        targetY = Math.max(0, Math.min(targetY, screenHeight - height));
    }
    
    public void update() {
        // Smooth movement towards target
        float dx = targetX - x;
        float dy = targetY - y;
        
        if (Math.abs(dx) > 2) {
            x += dx * 0.15f; // Smooth interpolation
        } else {
            x = targetX;
        }
        
        if (Math.abs(dy) > 2) {
            y += dy * 0.15f;
        } else {
            y = targetY;
        }
    }
    
    public void draw(Canvas canvas) {
        if (spaceShipBitmap != null) {
            // Draw the spaceship bitmap
            Rect srcRect = new Rect(0, 0, spaceShipBitmap.getWidth(), spaceShipBitmap.getHeight());
            Rect dstRect = new Rect((int)x, (int)y, (int)x + width, (int)y + height);
            canvas.drawBitmap(spaceShipBitmap, srcRect, dstRect, paint);
        } else {
            // Fallback: draw a simple triangle
            paint.setColor(Color.CYAN);
            
            float[] points = {
                x, y + height / 2f,           // Left point
                x + width, y,                 // Top right
                x + width, y + height,        // Bottom right
                x, y + height / 2f           // Back to left
            };
            
            canvas.drawLines(points, paint);
        }
    }
    
    public boolean checkCollision(Rect otherBounds) {
        return getCollisionBounds().intersect(otherBounds);
    }
    
    public Rect getBounds() {
        return new Rect((int)x, (int)y, (int)x + width, (int)y + height);
    }
    
    public Rect getCollisionBounds() {
        // Reduced collision area for fairer gameplay
        int collisionWidth = (int)(width * 0.6);
        int collisionHeight = (int)(height * 0.6);
        int offsetX = (width - collisionWidth) / 2;
        int offsetY = (height - collisionHeight) / 2;
        
        return new Rect(
            (int)x + offsetX, 
            (int)y + offsetY, 
            (int)x + offsetX + collisionWidth, 
            (int)y + offsetY + collisionHeight
        );
    }
    
    // Getters
    public float getX() { return x; }
    public float getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
} 