package com.juegofeli.game;

import javax.sound.midi.*;

public class MusicPlayer {
    private Sequencer sequencer;
    private Sequence sequence;
    private boolean isPlaying = false;
    
    public MusicPlayer() {
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            createSpaceMusic();
        } catch (MidiUnavailableException e) {
            System.err.println("Error: No se pudo inicializar el reproductor MIDI");
            e.printStackTrace();
        }
    }
    
    private void createSpaceMusic() {
        try {
            // Crear una secuencia MIDI
            sequence = new Sequence(Sequence.PPQ, 24);
            Track track = sequence.createTrack();
            
            // Configurar instrumento (sintetizador espacial)
            ShortMessage programChange = new ShortMessage();
            programChange.setMessage(ShortMessage.PROGRAM_CHANGE, 0, 91, 0); // Pad 4 (choir) - sonido espacial
            track.add(new MidiEvent(programChange, 0));
            
            // Crear melodía espacial
            createSpaceMelody(track);
            
        } catch (InvalidMidiDataException e) {
            System.err.println("Error creando la música MIDI");
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
    
    public void playMusic() {
        try {
            if (sequencer != null && sequence != null && !isPlaying) {
                sequencer.setSequence(sequence);
                sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY); // Reproducir en loop
                sequencer.start();
                isPlaying = true;
                System.out.println("Música espacial iniciada");
            }
        } catch (InvalidMidiDataException e) {
            System.err.println("Error reproduciendo música");
            e.printStackTrace();
        }
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
        if (sequencer != null && sequence != null && isPlaying) {
            sequencer.start();
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