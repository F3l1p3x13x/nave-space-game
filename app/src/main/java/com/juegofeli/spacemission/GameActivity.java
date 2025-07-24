package com.juegofeli.spacemission;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.juegofeli.spacemission.audio.MusicManager;
import com.juegofeli.spacemission.game.GameEngine;
import com.juegofeli.spacemission.views.GameView;

public class GameActivity extends AppCompatActivity implements GameEngine.GameListener {
    
    private GameView gameView;
    private GameEngine gameEngine;
    private MusicManager musicManager;
    
    // UI Elements
    private TextView scoreText;
    private TextView levelText;
    private TextView nextLevelText;
    private TextView livesText;
    private TextView meteoriteWarning;
    private Button pauseButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Configurar pantalla completa
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        setContentView(R.layout.activity_game);
        
        // Inicializar componentes
        initializeComponents();
        
        // Configurar juego
        setupGame();
        
        // Configurar interacciones
        setupInteractions();
    }
    
    private void initializeComponents() {
        gameView = findViewById(R.id.gameView);
        scoreText = findViewById(R.id.scoreText);
        levelText = findViewById(R.id.levelText);
        nextLevelText = findViewById(R.id.nextLevelText);
        livesText = findViewById(R.id.livesText);
        meteoriteWarning = findViewById(R.id.meteoriteWarning);
        pauseButton = findViewById(R.id.pauseButton);
        
        musicManager = new MusicManager(this);
    }
    
    private void setupGame() {
        // Crear engine del juego
        gameEngine = new GameEngine(this, this);
        
        // Configurar la vista del juego
        gameView.setGameEngine(gameEngine);
        
        // Iniciar música del juego
        musicManager.playGameMusic();
        
        // Iniciar el juego
        gameEngine.startGame();
    }
    
    private void setupInteractions() {
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePause();
            }
        });
    }
    
    private void togglePause() {
        if (gameEngine.isPaused()) {
            gameEngine.resumeGame();
            pauseButton.setText("⏸");
            musicManager.resumeMusic();
        } else {
            gameEngine.pauseGame();
            pauseButton.setText("▶");
            musicManager.pauseMusic();
        }
    }
    
    // Métodos del GameEngine.GameListener
    @Override
    public void onScoreUpdate(int score) {
        runOnUiThread(() -> {
            scoreText.setText("Tiempo: " + score + "s");
        });
    }
    
    @Override
    public void onLevelUpdate(int level, int timeUntilNext) {
        runOnUiThread(() -> {
            levelText.setText("Nivel: " + level);
            nextLevelText.setText("Próximo nivel en: " + timeUntilNext + "s");
        });
    }
    
    @Override
    public void onLivesUpdate(int lives) {
        runOnUiThread(() -> {
            StringBuilder livesDisplay = new StringBuilder("Vidas: ");
            for (int i = 0; i < lives; i++) {
                livesDisplay.append("♥ ");
            }
            for (int i = lives; i < 3; i++) {
                livesDisplay.append("♡ ");
            }
            livesText.setText(livesDisplay.toString());
        });
    }
    
    @Override
    public void onMeteoriteWarning(boolean active, int count) {
        runOnUiThread(() -> {
            if (active) {
                meteoriteWarning.setVisibility(View.VISIBLE);
                meteoriteWarning.setText(getString(R.string.meteorite_warning) + "\nActivos: " + count);
            } else {
                meteoriteWarning.setVisibility(View.GONE);
            }
        });
    }
    
    @Override
    public void onGameOver(int finalScore) {
        runOnUiThread(() -> {
            musicManager.stopMusic();
            // Volver a la actividad principal
            finish();
        });
    }
    
    @Override
    public void onLifeLost(int remainingLives) {
        runOnUiThread(() -> {
            // Aquí podrías mostrar un diálogo o animación
            // Por ahora solo actualizamos las vidas
            onLivesUpdate(remainingLives);
        });
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (gameView != null) {
            gameView.resume();
        }
        if (musicManager != null && !gameEngine.isPaused()) {
            musicManager.resumeMusic();
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        if (gameView != null) {
            gameView.pause();
        }
        if (musicManager != null) {
            musicManager.pauseMusic();
        }
        if (gameEngine != null) {
            gameEngine.pauseGame();
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gameEngine != null) {
            gameEngine.cleanup();
        }
        if (musicManager != null) {
            musicManager.cleanup();
        }
    }
    
    @Override
    public void onBackPressed() {
        // Pausar el juego cuando se presiona atrás
        if (gameEngine != null && !gameEngine.isPaused()) {
            togglePause();
        } else {
            super.onBackPressed();
        }
    }
} 