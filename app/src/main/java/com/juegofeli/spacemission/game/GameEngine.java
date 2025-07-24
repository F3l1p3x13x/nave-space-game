package com.juegofeli.spacemission.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Vibrator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.juegofeli.spacemission.R;

public class GameEngine {
    
    // Game dimensions
    private int screenWidth;
    private int screenHeight;
    
    // Game objects
    private SpaceShipAndroid spaceShip;
    private List<ObstacleAndroid> obstacles;
    private List<MeteoiteAndroid> meteorites;
    private List<BulletAndroid> bullets;
    
    // Game state
    private boolean isRunning = false;
    private boolean isPaused = false;
    private long startTime;
    private long currentTime;
    private int score;
    private int lives;
    private static final int MAX_LIVES = 3;
    private int currentLevelScore;
    
    // Difficulty system
    private int difficultyLevel;
    private long lastDifficultyIncrease;
    private int obstacleSpawnTimer;
    private int obstacleSpawnDelay;
    private int meteoriteSpawnTimer;
    
    // Shooting system
    private int shootCooldown;
    private static final int SHOOT_COOLDOWN_TIME = 30; // 30 frames = ~500ms for touch
    
    // Touch input
    private float targetX, targetY;
    private boolean isTouchingShooting = false;
    private long touchDownTime = 0;
    private static final long SHOOT_TOUCH_DELAY = 200; // 200ms to start shooting
    
    // Listener for UI updates
    private GameListener gameListener;
    
    // Resources
    private Context context;
    private Random random;
    private Paint paint;
    private Vibrator vibrator;
    
    // Sprites
    private Bitmap spaceShipBitmap;
    private Bitmap felipeBitmap;
    
    public interface GameListener {
        void onScoreUpdate(int score);
        void onLevelUpdate(int level, int timeUntilNext);
        void onLivesUpdate(int lives);
        void onMeteoriteWarning(boolean active, int count);
        void onGameOver(int finalScore);
        void onLifeLost(int remainingLives);
    }
    
    public GameEngine(Context context, GameListener listener) {
        this.context = context;
        this.gameListener = listener;
        this.random = new Random();
        this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        
        // Initialize vibrator for haptic feedback
        this.vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        
        // Initialize game objects
        initializeGame();
        
        // Load sprites
        loadSprites();
    }
    
    private void initializeGame() {
        obstacles = new ArrayList<>();
        meteorites = new ArrayList<>();
        bullets = new ArrayList<>();
        
        lives = MAX_LIVES;
        difficultyLevel = 1;
        score = 0;
        currentLevelScore = 0;
        obstacleSpawnDelay = 120; // ~2 seconds at 60fps
        obstacleSpawnTimer = 0;
        meteoriteSpawnTimer = 0;
        shootCooldown = 0;
    }
    
    private void loadSprites() {
        try {
            spaceShipBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.nave_space_ship);
            felipeBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.felipe);
        } catch (Exception e) {
            // Handle missing resources gracefully
        }
    }
    
    public void setScreenSize(int width, int height) {
        this.screenWidth = width;
        this.screenHeight = height;
        
        // Initialize spaceship
        if (spaceShip == null) {
            spaceShip = new SpaceShipAndroid(screenWidth, screenHeight, spaceShipBitmap);
        }
    }
    
    public void startGame() {
        isRunning = true;
        isPaused = false;
        startTime = System.currentTimeMillis();
        lastDifficultyIncrease = startTime;
        
        // Reset game state
        obstacles.clear();
        meteorites.clear();
        bullets.clear();
        lives = MAX_LIVES;
        difficultyLevel = 1;
        score = 0;
        currentLevelScore = 0;
        
        // Reset spaceship position
        if (spaceShip != null) {
            spaceShip.resetPosition();
        }
        
        // Notify UI
        updateUI();
    }
    
    public void pauseGame() {
        isPaused = true;
    }
    
    public void resumeGame() {
        isPaused = false;
    }
    
    public boolean isPaused() {
        return isPaused;
    }
    
    // Touch input handlers
    public void onTouchDown(float x, float y) {
        targetX = x;
        targetY = y;
        touchDownTime = System.currentTimeMillis();
    }
    
    public void onTouchMove(float x, float y) {
        targetX = x;
        targetY = y;
        
        // Move spaceship towards touch position
        if (spaceShip != null) {
            spaceShip.moveTowards(x, y);
        }
        
        // Check if we should start shooting (touch held for more than delay)
        if (System.currentTimeMillis() - touchDownTime > SHOOT_TOUCH_DELAY) {
            isTouchingShooting = true;
        }
    }
    
    public void onTouchUp(float x, float y) {
        isTouchingShooting = false;
        touchDownTime = 0;
    }
    
    public void update() {
        if (!isRunning || isPaused) return;
        
        currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - startTime;
        score = (int) (elapsedTime / 1000);
        
        // Check for difficulty increase (every 20 seconds)
        if (currentTime - lastDifficultyIncrease >= 20000) {
            increaseDifficulty();
            lastDifficultyIncrease = currentTime;
        }
        
        // Update spaceship
        if (spaceShip != null) {
            spaceShip.update();
        }
        
        // Handle shooting
        handleShooting();
        
        // Spawn obstacles
        spawnObstacles();
        
        // Spawn meteorites (from level 6)
        if (difficultyLevel >= 6) {
            spawnMeteorites();
        }
        
        // Update game objects
        updateObstacles();
        updateMeteorites();
        updateBullets();
        
        // Update UI
        updateUI();
    }
    
    private void handleShooting() {
        if (shootCooldown > 0) {
            shootCooldown--;
        }
        
        if (isTouchingShooting && shootCooldown <= 0 && spaceShip != null) {
            // Create bullet
            float bulletX = spaceShip.getX() + spaceShip.getWidth();
            float bulletY = spaceShip.getY() + spaceShip.getHeight() / 2f;
            
            bullets.add(new BulletAndroid(bulletX, bulletY, screenWidth));
            shootCooldown = SHOOT_COOLDOWN_TIME;
        }
    }
    
    private void spawnObstacles() {
        obstacleSpawnTimer++;
        if (obstacleSpawnTimer >= obstacleSpawnDelay) {
            obstacles.add(new ObstacleAndroid(screenWidth, screenHeight, difficultyLevel));
            obstacleSpawnTimer = 0;
        }
    }
    
    private void spawnMeteorites() {
        meteoriteSpawnTimer++;
        int meteoriteSpawnChance = 180 - (difficultyLevel - 6) * 20;
        if (meteoriteSpawnChance < 60) meteoriteSpawnChance = 60;
        
        if (meteoriteSpawnTimer >= meteoriteSpawnChance && random.nextFloat() < 0.4f) {
            meteorites.add(new MeteoiteAndroid(screenWidth, screenHeight));
            meteoriteSpawnTimer = 0;
        }
    }
    
    private void updateObstacles() {
        Iterator<ObstacleAndroid> iterator = obstacles.iterator();
        while (iterator.hasNext()) {
            ObstacleAndroid obstacle = iterator.next();
            obstacle.update();
            
            if (obstacle.isOffScreen()) {
                iterator.remove();
            } else if (spaceShip != null && spaceShip.checkCollision(obstacle.getCollisionBounds())) {
                loseLife();
                return;
            }
        }
    }
    
    private void updateMeteorites() {
        Iterator<MeteoiteAndroid> iterator = meteorites.iterator();
        while (iterator.hasNext()) {
            MeteoiteAndroid meteorite = iterator.next();
            meteorite.update();
            
            if (meteorite.isOffScreen(screenWidth, screenHeight) || meteorite.isExpired()) {
                iterator.remove();
            } else if (spaceShip != null && spaceShip.checkCollision(meteorite.getCollisionBounds())) {
                loseLife();
                return;
            }
        }
    }
    
    private void updateBullets() {
        Iterator<BulletAndroid> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            BulletAndroid bullet = bulletIterator.next();
            bullet.update();
            
            if (!bullet.isActive()) {
                bulletIterator.remove();
                continue;
            }
            
            // Check collisions with obstacles
            Iterator<ObstacleAndroid> obstacleIterator = obstacles.iterator();
            while (obstacleIterator.hasNext()) {
                ObstacleAndroid obstacle = obstacleIterator.next();
                if (bullet.getBounds().intersect(obstacle.getBounds())) {
                    bulletIterator.remove();
                    obstacleIterator.remove();
                    score += 10;
                    break;
                }
            }
            
            // Check collisions with meteorites
            if (bullet.isActive()) {
                Iterator<MeteoiteAndroid> meteoriteIterator = meteorites.iterator();
                while (meteoriteIterator.hasNext()) {
                    MeteoiteAndroid meteorite = meteoriteIterator.next();
                    if (bullet.getBounds().intersect(meteorite.getBounds())) {
                        bulletIterator.remove();
                        meteoriteIterator.remove();
                        score += 20;
                        break;
                    }
                }
            }
        }
    }
    
    private void loseLife() {
        lives--;
        
        // Vibrate for haptic feedback
        if (vibrator != null) {
            vibrator.vibrate(300);
        }
        
        if (lives <= 0) {
            // Game over
            isRunning = false;
            if (gameListener != null) {
                gameListener.onGameOver(score);
            }
        } else {
            // Lose life but continue
            if (gameListener != null) {
                gameListener.onLifeLost(lives);
            }
            
            // Reset level but keep progress
            resetCurrentLevel();
        }
    }
    
    private void resetCurrentLevel() {
        score = currentLevelScore;
        obstacles.clear();
        meteorites.clear();
        bullets.clear();
        
        if (spaceShip != null) {
            spaceShip.resetPosition();
        }
    }
    
    private void increaseDifficulty() {
        difficultyLevel++;
        currentLevelScore = score;
        
        // Increase spawn rate
        if (obstacleSpawnDelay > 40) {
            obstacleSpawnDelay -= 12;
        } else if (obstacleSpawnDelay > 25) {
            obstacleSpawnDelay -= 5;
        }
        
        if (obstacleSpawnDelay < 25) {
            obstacleSpawnDelay = 25;
        }
    }
    
    private void updateUI() {
        if (gameListener != null) {
            gameListener.onScoreUpdate(score);
            
            long timeUntilNext = 20 - ((currentTime - lastDifficultyIncrease) / 1000);
            gameListener.onLevelUpdate(difficultyLevel, (int)timeUntilNext);
            
            gameListener.onLivesUpdate(lives);
            
            gameListener.onMeteoriteWarning(difficultyLevel >= 6, meteorites.size());
        }
    }
    
    public void render(Canvas canvas) {
        if (canvas == null) return;
        
        // Clear screen
        canvas.drawColor(Color.BLACK);
        
        // Draw space background
        drawSpaceBackground(canvas);
        
        // Draw spaceship
        if (spaceShip != null) {
            spaceShip.draw(canvas);
        }
        
        // Draw obstacles
        for (ObstacleAndroid obstacle : obstacles) {
            obstacle.draw(canvas);
        }
        
        // Draw meteorites
        for (MeteoiteAndroid meteorite : meteorites) {
            meteorite.draw(canvas);
        }
        
        // Draw bullets
        for (BulletAndroid bullet : bullets) {
            bullet.draw(canvas);
        }
    }
    
    private void drawSpaceBackground(Canvas canvas) {
        // Simple space background
        paint.setColor(Color.BLACK);
        canvas.drawRect(0, 0, screenWidth, screenHeight, paint);
        
        // Draw some static stars
        paint.setColor(Color.WHITE);
        for (int i = 0; i < 50; i++) {
            float x = (i * 137) % screenWidth;
            float y = (i * 211) % screenHeight;
            canvas.drawCircle(x, y, 1, paint);
        }
    }
    
    public void cleanup() {
        isRunning = false;
        isPaused = false;
    }
} 