package com.juegofeli.spacemission.audio;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

public class MusicManager {
    
    private static final String TAG = "MusicManager";
    
    private Context context;
    private MediaPlayer introMediaPlayer;
    private MediaPlayer gameMediaPlayer;
    private MediaPlayer currentPlayer;
    private boolean isPlaying = false;
    private boolean isPaused = false;
    
    public MusicManager(Context context) {
        this.context = context;
        initializePlayers();
    }
    
    private void initializePlayers() {
        try {
            // Por ahora usaremos sonidos del sistema Android
            // En una implementación completa, incluirías archivos de audio en res/raw/
            
            // Para la demostración, crearemos players silenciosos
            // que pueden ser reemplazados con archivos de audio reales
            
            Log.d(TAG, "MusicManager inicializado (modo silencioso)");
        } catch (Exception e) {
            Log.e(TAG, "Error inicializando reproductores de música", e);
        }
    }
    
    public void playIntroMusic() {
        try {
            stopMusic();
            
            // Aquí cargarías el archivo de música de introducción
            // introMediaPlayer = MediaPlayer.create(context, R.raw.intro_music);
            // if (introMediaPlayer != null) {
            //     introMediaPlayer.setLooping(true);
            //     introMediaPlayer.start();
            //     currentPlayer = introMediaPlayer;
            //     isPlaying = true;
            // }
            
            Log.d(TAG, "Música de introducción iniciada (silenciosa)");
            isPlaying = true;
            
        } catch (Exception e) {
            Log.e(TAG, "Error reproduciendo música de introducción", e);
        }
    }
    
    public void playGameMusic() {
        try {
            stopMusic();
            
            // Aquí cargarías el archivo de música del juego
            // gameMediaPlayer = MediaPlayer.create(context, R.raw.game_music);
            // if (gameMediaPlayer != null) {
            //     gameMediaPlayer.setLooping(true);
            //     gameMediaPlayer.start();
            //     currentPlayer = gameMediaPlayer;
            //     isPlaying = true;
            // }
            
            Log.d(TAG, "Música del juego iniciada (silenciosa)");
            isPlaying = true;
            
        } catch (Exception e) {
            Log.e(TAG, "Error reproduciendo música del juego", e);
        }
    }
    
    public void pauseMusic() {
        try {
            if (currentPlayer != null && isPlaying && !isPaused) {
                currentPlayer.pause();
                isPaused = true;
                Log.d(TAG, "Música pausada");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error pausando música", e);
        }
    }
    
    public void resumeMusic() {
        try {
            if (currentPlayer != null && isPaused) {
                currentPlayer.start();
                isPaused = false;
                Log.d(TAG, "Música reanudada");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error reanudando música", e);
        }
    }
    
    public void stopMusic() {
        try {
            if (currentPlayer != null && isPlaying) {
                currentPlayer.stop();
                currentPlayer.release();
                currentPlayer = null;
                isPlaying = false;
                isPaused = false;
                Log.d(TAG, "Música detenida");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error deteniendo música", e);
        }
    }
    
    public void cleanup() {
        try {
            stopMusic();
            
            if (introMediaPlayer != null) {
                introMediaPlayer.release();
                introMediaPlayer = null;
            }
            
            if (gameMediaPlayer != null) {
                gameMediaPlayer.release();
                gameMediaPlayer = null;
            }
            
            Log.d(TAG, "Recursos de audio liberados");
        } catch (Exception e) {
            Log.e(TAG, "Error limpiando recursos de audio", e);
        }
    }
    
    public boolean isPlaying() {
        return isPlaying && !isPaused;
    }
} 