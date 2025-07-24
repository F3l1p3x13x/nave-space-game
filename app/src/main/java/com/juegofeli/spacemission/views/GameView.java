package com.juegofeli.spacemission.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.juegofeli.spacemission.game.GameEngine;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    
    private GameEngine gameEngine;
    private SurfaceHolder holder;
    private Paint paint;
    private boolean isRunning = false;
    private Thread renderThread;
    
    // Touch input
    private float touchX = 0;
    private float touchY = 0;
    private boolean isTouching = false;
    private boolean isShootingTouch = false;
    
    public GameView(Context context) {
        super(context);
        init();
    }
    
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    
    private void init() {
        holder = getHolder();
        holder.addCallback(this);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        setFocusable(true);
    }
    
    public void setGameEngine(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }
    
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // Surface está listo
    }
    
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (gameEngine != null) {
            gameEngine.setScreenSize(width, height);
        }
    }
    
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        pause();
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handleTouchDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                handleTouchMove(event);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                handleTouchUp(event);
                break;
        }
        return true;
    }
    
    private void handleTouchDown(MotionEvent event) {
        touchX = event.getX();
        touchY = event.getY();
        isTouching = true;
        
        // Determinar si es un toque para disparar (mantener presionado)
        // o para mover (toque simple)
        if (gameEngine != null) {
            gameEngine.onTouchDown(touchX, touchY);
        }
    }
    
    private void handleTouchMove(MotionEvent event) {
        if (isTouching) {
            touchX = event.getX();
            touchY = event.getY();
            
            if (gameEngine != null) {
                gameEngine.onTouchMove(touchX, touchY);
            }
        }
    }
    
    private void handleTouchUp(MotionEvent event) {
        isTouching = false;
        isShootingTouch = false;
        
        if (gameEngine != null) {
            gameEngine.onTouchUp(event.getX(), event.getY());
        }
    }
    
    public void resume() {
        isRunning = true;
        renderThread = new Thread(this::runRenderLoop);
        renderThread.start();
    }
    
    public void pause() {
        isRunning = false;
        if (renderThread != null) {
            try {
                renderThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    private void runRenderLoop() {
        while (isRunning) {
            Canvas canvas = null;
            try {
                canvas = holder.lockCanvas();
                if (canvas != null && gameEngine != null) {
                    // Actualizar juego
                    gameEngine.update();
                    
                    // Renderizar juego
                    gameEngine.render(canvas);
                }
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
            }
            
            try {
                Thread.sleep(16); // ~60 FPS
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    
    // Métodos para obtener el estado del touch (para el GameEngine)
    public float getTouchX() {
        return touchX;
    }
    
    public float getTouchY() {
        return touchY;
    }
    
    public boolean isTouching() {
        return isTouching;
    }
} 