package com.juegofeli.spacemission.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class BulletAndroid {
    private float x, y;
    private int width, height;
    private float speed;
    private int screenWidth;
    private boolean active;
    private Paint paint;
    
    public BulletAndroid(float startX, float startY, int screenWidth) {
        this.x = startX;
        this.y = startY;
        this.screenWidth = screenWidth;
        this.width = 12;
        this.height = 6;
        this.speed = 15; // Fast for mobile
        this.active = true;
        this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }
    
    public void update() {
        if (active) {
            x += speed;
            
            // Deactivate if off screen
            if (x > screenWidth) {
                active = false;
            }
        }
    }
    
    public void draw(Canvas canvas) {
        if (active) {
            // Draw laser bullet with glow effect
            
            // Outer glow (cyan)
            paint.setColor(Color.CYAN);
            paint.setAlpha(100);
            canvas.drawRect(x - 2, y - 1, x + width + 2, y + height + 1, paint);
            
            // Main bullet (white)
            paint.setColor(Color.WHITE);
            paint.setAlpha(255);
            canvas.drawRect(x, y, x + width, y + height, paint);
            
            // Inner core (bright cyan)
            paint.setColor(Color.CYAN);
            canvas.drawRect(x + 2, y + 1, x + width - 2, y + height - 1, paint);
            
            // Speed lines for effect
            paint.setColor(Color.CYAN);
            paint.setAlpha(150);
            canvas.drawLine(x - 5, y + height / 2f, x - 10, y + height / 2f, paint);
            canvas.drawLine(x - 3, y + height / 2f - 1, x - 6, y + height / 2f - 1, paint);
            canvas.drawLine(x - 3, y + height / 2f + 1, x - 6, y + height / 2f + 1, paint);
            
            paint.setAlpha(255);
        }
    }
    
    public Rect getBounds() {
        return new Rect((int)x, (int)y, (int)x + width, (int)y + height);
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public float getX() { return x; }
    public float getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
} 