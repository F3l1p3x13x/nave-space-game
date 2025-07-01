package com.juegofeli.game;

import javax.sound.midi.*;

public class MusicPlayer {
    private Sequencer sequencer;
    private Sequence gameSequence;
    private Sequence introSequence;
    private boolean isPlaying = false;
    private boolean isIntroMusic = false;
    
    public MusicPlayer() {
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            createGameMusic();
            createIntroMusic();
        } catch (MidiUnavailableException e) {
            System.err.println("Error: No se pudo inicializar el reproductor MIDI");
            e.printStackTrace();
        }
    }
    
    private void createGameMusic() {
        try {
            // Crear una secuencia MIDI para el juego
            gameSequence = new Sequence(Sequence.PPQ, 24);
            Track track = gameSequence.createTrack();
            
            // Configurar instrumento (sintetizador espacial)
            ShortMessage programChange = new ShortMessage();
            programChange.setMessage(ShortMessage.PROGRAM_CHANGE, 0, 91, 0); // Pad 4 (choir) - sonido espacial
            track.add(new MidiEvent(programChange, 0));
            
            // Crear melodía espacial para el juego
            createSpaceMelody(track);
            
        } catch (InvalidMidiDataException e) {
            System.err.println("Error creando la música MIDI del juego");
            e.printStackTrace();
        }
    }
    
    private void createIntroMusic() {
        try {
            // Crear una secuencia MIDI épica para la pantalla inicial
            introSequence = new Sequence(Sequence.PPQ, 24);
            Track track = introSequence.createTrack();
            
            // Configurar instrumento principal (coro espacial)
            ShortMessage programChange = new ShortMessage();
            programChange.setMessage(ShortMessage.PROGRAM_CHANGE, 0, 52, 0); // Choir Aahs - sonido épico
            track.add(new MidiEvent(programChange, 0));
            
            // Crear melodía épica de introducción
            createEpicIntroMelody(track);
            
        } catch (InvalidMidiDataException e) {
            System.err.println("Error creando la música MIDI de introducción");
            e.printStackTrace();
        }
    }
    
    private void createSpaceMelody(Track track) throws InvalidMidiDataException {
        // Notas para crear una melodía espacial misteriosa
        int[] spaceMelody1 = {
            60, 64, 67, 72, 76, 79, 84, 79, 76, 72, 67, 64, // Escala ascendente y descendente
            60, 55, 52, 48, 52, 55, 60, 64, 67, 64, 60, 57  // Variación
        };
        
        int[] spaceMelody2 = {
            72, 76, 79, 84, 88, 84, 79, 76, 72, 69, 65, 62, // Melodía en octava alta
            69, 65, 62, 58, 62, 65, 69, 72, 76, 72, 69, 65  // Respuesta
        };
        
        int tick = 0;
        int noteDuration = 12; // Duración de cada nota
        
        // Crear la melodía principal (canal 0)
        for (int i = 0; i < spaceMelody1.length; i++) {
            // Nota ON
            ShortMessage noteOn = new ShortMessage();
            noteOn.setMessage(ShortMessage.NOTE_ON, 0, spaceMelody1[i], 60); // Volumen medio
            track.add(new MidiEvent(noteOn, tick));
            
            // Nota OFF
            ShortMessage noteOff = new ShortMessage();
            noteOff.setMessage(ShortMessage.NOTE_OFF, 0, spaceMelody1[i], 0);
            track.add(new MidiEvent(noteOff, tick + noteDuration));
            
            tick += noteDuration + 2; // Pequeña pausa entre notas
        }
        
        // Configurar segundo canal con un instrumento diferente
        ShortMessage programChange2 = new ShortMessage();
        programChange2.setMessage(ShortMessage.PROGRAM_CHANGE, 1, 89, 0); // Pad 2 (warm pad)
        track.add(new MidiEvent(programChange2, 24));
        
        // Crear armonía en segundo canal
        tick = 24; // Empezar un poco después
        for (int i = 0; i < spaceMelody2.length; i++) {
            // Nota ON
            ShortMessage noteOn = new ShortMessage();
            noteOn.setMessage(ShortMessage.NOTE_ON, 1, spaceMelody2[i], 40); // Volumen más bajo para armonía
            track.add(new MidiEvent(noteOn, tick));
            
            // Nota OFF
            ShortMessage noteOff = new ShortMessage();
            noteOff.setMessage(ShortMessage.NOTE_OFF, 1, spaceMelody2[i], 0);
            track.add(new MidiEvent(noteOff, tick + noteDuration * 2)); // Notas más largas para armonía
            
            tick += noteDuration * 2 + 4;
        }
        
        // Agregar algunos efectos de ambiente
        addAmbientEffects(track);
    }
    
    private void addAmbientEffects(Track track) throws InvalidMidiDataException {
        // Configurar canal 2 para efectos ambientales
        ShortMessage programChange3 = new ShortMessage();
        programChange3.setMessage(ShortMessage.PROGRAM_CHANGE, 2, 95, 0); // FX 8 (sci-fi)
        track.add(new MidiEvent(programChange3, 0));
        
        // Crear efectos espaciales aleatorios
        int[] ambientNotes = {36, 48, 60, 72, 84}; // Notas graves y agudas para ambiente
        
        for (int i = 0; i < 8; i++) {
            int tick = i * 48 + (int)(Math.random() * 24); // Posiciones aleatorias
            int note = ambientNotes[(int)(Math.random() * ambientNotes.length)];
            
            ShortMessage noteOn = new ShortMessage();
            noteOn.setMessage(ShortMessage.NOTE_ON, 2, note, 20); // Volumen muy bajo
            track.add(new MidiEvent(noteOn, tick));
            
            ShortMessage noteOff = new ShortMessage();
            noteOff.setMessage(ShortMessage.NOTE_OFF, 2, note, 0);
            track.add(new MidiEvent(noteOff, tick + 36)); // Notas largas para ambiente
        }
    }
    
    private void createEpicIntroMelody(Track track) throws InvalidMidiDataException {
        // Melodía épica y cinematográfica para inspirar un viaje estelar
        int[] epicMelody = {
            // Frase 1: Ascendente y majestuosa
            48, 52, 55, 60, 64, 67, 72, 76, 79, 84,
            // Frase 2: Descendente con poder
            84, 79, 76, 72, 67, 64, 60, 55, 52, 48,
            // Frase 3: Saltos épicos
            60, 72, 64, 76, 67, 79, 72, 84, 76, 88,
            // Frase 4: Resolución heroica
            84, 79, 76, 72, 69, 65, 62, 60, 64, 67, 72
        };
        
        int tick = 0;
        int noteDuration = 18; // Notas más largas para efecto épico
        
        // Crear la melodía principal épica
        for (int i = 0; i < epicMelody.length; i++) {
            // Nota ON con velocidad variable para expresión
            int velocity = 80 + (i % 3) * 15; // Variación dinámica
            ShortMessage noteOn = new ShortMessage();
            noteOn.setMessage(ShortMessage.NOTE_ON, 0, epicMelody[i], velocity);
            track.add(new MidiEvent(noteOn, tick));
            
            // Nota OFF
            ShortMessage noteOff = new ShortMessage();
            noteOff.setMessage(ShortMessage.NOTE_OFF, 0, epicMelody[i], 0);
            track.add(new MidiEvent(noteOff, tick + noteDuration));
            
            tick += noteDuration + 3; // Pequeña pausa entre notas
        }
        
        // Agregar armonías épicas
        addEpicHarmony(track);
        
        // Agregar efectos de ambiente cósmico
        addCosmicEffects(track);
    }
    
    private void addEpicHarmony(Track track) throws InvalidMidiDataException {
        // Configurar segundo canal con cuerdas
        ShortMessage programChange2 = new ShortMessage();
        programChange2.setMessage(ShortMessage.PROGRAM_CHANGE, 1, 48, 0); // String Ensemble
        track.add(new MidiEvent(programChange2, 0));
        
        // Acordes de apoyo épicos
        int[] harmonyNotes = {36, 40, 43, 48, 52, 55, 60, 64, 67, 72};
        int tick = 12; // Empezar ligeramente después
        
        for (int i = 0; i < harmonyNotes.length; i++) {
            ShortMessage noteOn = new ShortMessage();
            noteOn.setMessage(ShortMessage.NOTE_ON, 1, harmonyNotes[i], 60);
            track.add(new MidiEvent(noteOn, tick));
            
            ShortMessage noteOff = new ShortMessage();
            noteOff.setMessage(ShortMessage.NOTE_OFF, 1, harmonyNotes[i], 0);
            track.add(new MidiEvent(noteOff, tick + 36)); // Notas largas para armonía
            
            tick += 42;
        }
    }
    
    private void addCosmicEffects(Track track) throws InvalidMidiDataException {
        // Configurar canal 2 para efectos cósmicos
        ShortMessage programChange3 = new ShortMessage();
        programChange3.setMessage(ShortMessage.PROGRAM_CHANGE, 2, 103, 0); // FX 8 (sci-fi)
        track.add(new MidiEvent(programChange3, 0));
        
        // Crear efectos cósmicos inspiradores
        int[] cosmicNotes = {24, 36, 48, 60, 72, 84, 96}; // Notas muy graves y muy agudas
        
        for (int i = 0; i < 6; i++) {
            int tick = i * 60 + (int)(Math.random() * 30); // Posiciones semi-aleatorias
            int note = cosmicNotes[(int)(Math.random() * cosmicNotes.length)];
            
            ShortMessage noteOn = new ShortMessage();
            noteOn.setMessage(ShortMessage.NOTE_ON, 2, note, 25); // Volumen bajo para ambiente
            track.add(new MidiEvent(noteOn, tick));
            
            ShortMessage noteOff = new ShortMessage();
            noteOff.setMessage(ShortMessage.NOTE_OFF, 2, note, 0);
            track.add(new MidiEvent(noteOff, tick + 48)); // Notas largas para ambiente
        }
    }
    
    public void playGameMusic() {
        try {
            if (sequencer != null && gameSequence != null && !isPlaying) {
                sequencer.setSequence(gameSequence);
                sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
                sequencer.start();
                isPlaying = true;
                isIntroMusic = false;
                System.out.println("Música del juego iniciada");
            }
        } catch (InvalidMidiDataException e) {
            System.err.println("Error reproduciendo música del juego");
            e.printStackTrace();
        }
    }
    
    public void playIntroMusic() {
        try {
            if (sequencer != null && introSequence != null && !isPlaying) {
                sequencer.setSequence(introSequence);
                sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
                sequencer.start();
                isPlaying = true;
                isIntroMusic = true;
                System.out.println("Música épica de introducción iniciada");
            }
        } catch (InvalidMidiDataException e) {
            System.err.println("Error reproduciendo música de introducción");
            e.printStackTrace();
        }
    }
    
    // Mantener método original para compatibilidad
    public void playMusic() {
        playGameMusic();
    }
    
    public void stopMusic() {
        if (sequencer != null && isPlaying) {
            sequencer.stop();
            isPlaying = false;
            System.out.println("Música detenida");
        }
    }
    
    public void pauseMusic() {
        if (sequencer != null && isPlaying) {
            sequencer.stop();
        }
    }
    
    public void resumeMusic() {
        if (sequencer != null && isPlaying) {
            if (isIntroMusic && introSequence != null) {
                sequencer.start();
            } else if (!isIntroMusic && gameSequence != null) {
                sequencer.start();
            }
        }
    }
    
    public boolean isPlaying() {
        return isPlaying && sequencer != null && sequencer.isRunning();
    }
    
    public void cleanup() {
        stopMusic();
        if (sequencer != null) {
            sequencer.close();
        }
    }
} 