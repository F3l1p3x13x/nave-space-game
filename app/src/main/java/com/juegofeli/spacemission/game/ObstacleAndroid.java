package com.juegofeli.spacemission.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import java.util.Random;

public class ObstacleAndroid {
    private float x, y;
    private int width, height;
    private float speed;
    private ObstacleType type;
    private int color;
    private Paint paint;
    private Random random;
    
    public enum ObstacleType {
        ASTEROID, PLANET
    }
    
    public ObstacleAndroid(int screenWidth, int screenHeight, int difficultyLevel) {
        this.random = new Random();
        this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        
        // Start from right side of screen
        this.x = screenWidth + random.nextInt(200);
        this.y = random.nextInt(screenHeight - 150);
        
        // Speed increases with difficulty
        int baseSpeed = 3 + random.nextInt(4);
        this.speed = baseSpeed + (difficultyLevel - 1) * 0.5f;
        
        // Random type
        this.type = random.nextBoolean() ? ObstacleType.ASTEROID : ObstacleType.PLANET;
        
        // Size increases with difficulty
        int sizeBonus = (difficultyLevel - 1) * 3;
        
        if (type == ObstacleType.ASTEROID) {
            this.width = 40 + random.nextInt(30) + sizeBonus;
            this.height = 40 + random.nextInt(30) + sizeBonus;
            this.color = Color.rgb(139, 69, 19); // Brown
        } else {
            this.width = 60 + random.nextInt(40) + sizeBonus;
            this.height = 60 + random.nextInt(40) + sizeBonus;
            // Random planet colors
            int[] planetColors = {
                Color.rgb(255, 69, 0),    // Red-orange
                Color.rgb(0, 128, 255),   // Blue
                Color.rgb(255, 215, 0),   // Gold
                Color.rgb(138, 43, 226),  // Purple
                Color.rgb(34, 139, 34)    // Green
            };
            this.color = planetColors[random.nextInt(planetColors.length)];
        }
    }
    
    public void update() {
        x -= speed;
    }
    
    public void draw(Canvas canvas) {
        paint.setColor(color);
        
        if (type == ObstacleType.ASTEROID) {
            // Draw asteroid as irregular shape
            canvas.drawRect(x, y, x + width, y + height, paint);
            
            // Add some texture
            paint.setColor(Color.BLACK);
            canvas.drawCircle(x + width * 0.3f, y + height * 0.3f, width * 0.1f, paint);
            canvas.drawCircle(x + width * 0.7f, y + height * 0.6f, width * 0.08f, paint);
            
        } else {
            // Draw planet as circle
            canvas.drawCircle(x + width / 2f, y + height / 2f, width / 2f, paint);
            
            // Add highlight
            paint.setColor(Color.WHITE);
            paint.setAlpha(100);
            canvas.drawCircle(x + width * 0.3f, y + height * 0.3f, width * 0.15f, paint);
            paint.setAlpha(255);
        }
    }
    
    public boolean isOffScreen() {
        return x + width < 0;
    }
    
    public Rect getBounds() {
        return new Rect((int)x, (int)y, (int)x + width, (int)y + height);
    }
    
    public Rect getCollisionBounds() {
        if (type == ObstacleType.ASTEROID) {
            // Asteroids: 75% of original area
            int collisionWidth = (int)(width * 0.75);
            int collisionHeight = (int)(height * 0.75);
            int offsetX = (width - collisionWidth) / 2;
            int offsetY = (height - collisionHeight) / 2;
            return new Rect((int)x + offsetX, (int)y + offsetY, 
                           (int)x + offsetX + collisionWidth, (int)y + offsetY + collisionHeight);
        } else {
            // Planets: 80% of original area
            int collisionWidth = (int)(width * 0.8);
            int collisionHeight = (int)(height * 0.8);
            int offsetX = (width - collisionWidth) / 2;
            int offsetY = (height - collisionHeight) / 2;
            return new Rect((int)x + offsetX, (int)y + offsetY, 
                           (int)x + offsetX + collisionWidth, (int)y + offsetY + collisionHeight);
        }
    }
    
    public float getX() { return x; }
    public float getY() { return y; }
    public ObstacleType getType() { return type; }
} 