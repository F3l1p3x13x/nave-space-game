package com.juegofeli.spacemission;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;

import androidx.appcompat.app.AppCompatActivity;

import com.juegofeli.spacemission.audio.MusicManager;
import com.juegofeli.spacemission.views.StarsView;

public class MainActivity extends AppCompatActivity {
    
    private MusicManager musicManager;
    private StarsView starsView;
    private TextView startInstruction;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Configurar pantalla completa
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        setContentView(R.layout.activity_main);
        
        // Inicializar componentes
        initializeComponents();
        
        // Configurar interacciones
        setupInteractions();
        
        // Iniciar animaciones
        startAnimations();
        
        // Iniciar música
        startMusic();
    }
    
    private void initializeComponents() {
        starsView = findViewById(R.id.starsView);
        startInstruction = findViewById(R.id.startInstruction);
        musicManager = new MusicManager(this);
    }
    
    private void setupInteractions() {
        Button startGameButton = findViewById(R.id.startGameButton);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });
    }
    
    private void startAnimations() {
        // Animación de parpadeo para la instrucción de inicio
        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(startInstruction, "alpha", 0.5f, 1.0f);
        alphaAnimation.setDuration(1500);
        alphaAnimation.setRepeatCount(ValueAnimator.INFINITE);
        alphaAnimation.setRepeatMode(ValueAnimator.REVERSE);
        alphaAnimation.start();
    }
    
    private void startMusic() {
        musicManager.playIntroMusic();
    }
    
    private void startGame() {
        // Detener música de introducción
        musicManager.stopMusic();
        
        // Iniciar actividad del juego
        Intent gameIntent = new Intent(this, GameActivity.class);
        startActivity(gameIntent);
        
        // Transición suave
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (starsView != null) {
            starsView.resume();
        }
        if (musicManager != null) {
            musicManager.resumeMusic();
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        if (starsView != null) {
            starsView.pause();
        }
        if (musicManager != null) {
            musicManager.pauseMusic();
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (musicManager != null) {
            musicManager.cleanup();
        }
    }
} 