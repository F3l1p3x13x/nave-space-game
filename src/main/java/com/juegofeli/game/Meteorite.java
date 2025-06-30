package com.juegofeli.game;

import java.awt.*;
import java.util.Random;

public class Meteorite {
    private float x, y;
    private float velocityX, velocityY;
    private int width, height;
    private Color color;
    private long creationTime;
    private static final int LIFETIME = 3000; // 3 segundos en milisegundos
    private Random random;
    private float rotation;
    private float rotationSpeed;
    
    public Meteorite(int gameWidth, int gameHeight) {
        this.random = new Random();
        this.creationTime = System.currentTimeMillis();
        
        // Tamaño aleatorio para meteoritos
        this.width = 8 + random.nextInt(12); // Entre 8 y 20 píxeles
        this.height = 8 + random.nextInt(12);
        
        // Posición inicial aleatoria en el borde superior
        this.x = random.nextInt(gameWidth + 100) - 50; // Puede empezar un poco fuera
        this.y = -height - random.nextInt(50); // Empieza arriba de la pantalla
        
        // Velocidad diagonal (hacia abajo y ligeramente hacia la izquierda)
        this.velocityX = -2 - random.nextFloat() * 3; // Entre -2 y -5
        this.velocityY = 3 + random.nextFloat() * 4;  // Entre 3 y 7
        
        // Color de meteorito (tonos rojizos/naranjas como fuego)
        Color[] meteoriteColors = {
            new Color(255, 69, 0),    // Rojo-naranja
            new Color(255, 140, 0),   // Naranja oscuro
            new Color(255, 165, 0),   // Naranja
            new Color(220, 20, 60),   // Rojo carmesí
            new Color(255, 215, 0)    // Dorado (muy caliente)
        };
        this.color = meteoriteColors[random.nextInt(meteoriteColors.length)];
        
        // Rotación para efecto visual
        this.rotation = 0;
        this.rotationSpeed = random.nextFloat() * 10 - 5; // Entre -5 y 5 grados por frame
    }
    
    public void update() {
        // Actualizar posición
        x += velocityX;
        y += velocityY;
        
        // Actualizar rotación
        rotation += rotationSpeed;
        if (rotation > 360) rotation -= 360;
        if (rotation < 0) rotation += 360;
    }
    
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Dibujar estela de fuego primero (detrás del meteorito)
        drawAdvancedTrail3D(g2d);
        
        // Aplicar rotación para el meteorito
        g2d.translate(x + width/2, y + height/2);
        g2d.rotate(Math.toRadians(rotation));
        
        // Sombra del meteorito
        g2d.setColor(new Color(0, 0, 0, 120));
        g2d.fillOval(-width/2 + 2, -height/2 + 2, width, height);
        
        // Meteorito principal con gradiente radial 3D
        RadialGradientPaint meteoriteGradient = new RadialGradientPaint(
            -width/4, -height/4, Math.max(width, height)/2,
            new float[]{0.0f, 0.5f, 1.0f},
            new Color[]{
                new Color(255, 255, 255, 255),    // Centro blanco ardiente
                color.brighter(),                  // Color base brillante
                color.darker()                     // Bordes más oscuros
            }
        );
        g2d.setPaint(meteoriteGradient);
        g2d.fillOval(-width/2, -height/2, width, height);
        
        // Textura irregular del meteorito
        drawMeteoriteTexture(g2d);
        
        // Halo de calor/energía
        g2d.setColor(new Color(color.getRed(), color.getGreen(), 0, 80));
        g2d.fillOval(-width/2 - 3, -height/2 - 3, width + 6, height + 6);
        
        // Núcleo ultra-brillante
        g2d.setColor(new Color(255, 255, 255, 200));
        g2d.fillOval(-3, -3, 6, 6);
        
        g2d.dispose();
    }
    
    private void drawMeteoriteTexture(Graphics2D g2d) {
        // Agregar textura rugosa al meteorito
        g2d.setColor(color.darker().darker());
        g2d.fillOval(-width/6, -height/6, width/8, height/8);
        g2d.fillOval(width/6, -height/8, width/10, height/10);
        g2d.fillOval(-width/8, height/6, width/12, height/12);
        
        // Grietas con brillo interno
        g2d.setColor(color.brighter().brighter());
        g2d.setStroke(new BasicStroke(1));
        g2d.drawLine(-width/4, -height/6, width/6, height/4);
        g2d.drawLine(-width/6, height/4, width/4, -height/8);
    }
    
    private void drawAdvancedTrail3D(Graphics2D g2d) {
        // Crear estela que va en dirección opuesta al movimiento
        float trailX = -velocityX * 3;
        float trailY = -velocityY * 3;
        
        // Dibujar múltiples capas de estela para efecto 3D
        for (int i = 0; i < 8; i++) {
            float progress = (float)i / 8.0f;
            float alpha = (1.0f - progress) * 0.8f; // Transparencia decreciente
            
            // Posición del segmento de estela
            float segmentX = x + width/2 + trailX * progress;
            float segmentY = y + height/2 + trailY * progress;
            int segmentSize = (int)((width + height) / 2 * (1.0f - progress) * 0.8f);
            
            if (segmentSize > 2) {
                // Gradiente radial para cada segmento de la estela
                RadialGradientPaint trailGradient = new RadialGradientPaint(
                    segmentX, segmentY, segmentSize/2,
                    new float[]{0.0f, 0.5f, 1.0f},
                    new Color[]{
                        new Color(255, 255, 200, (int)(alpha * 255)), // Centro amarillo brillante
                        new Color(color.getRed(), Math.min(255, color.getGreen() + 50), 0, (int)(alpha * 200)), // Naranja medio
                        new Color(color.getRed(), 0, 0, (int)(alpha * 100)) // Rojo exterior
                    }
                );
                g2d.setPaint(trailGradient);
                g2d.fillOval((int)(segmentX - segmentSize/2), (int)(segmentY - segmentSize/2), segmentSize, segmentSize);
                
                // Partículas ardientes adicionales
                if (i % 2 == 0) {
                    drawFireParticles(g2d, segmentX, segmentY, alpha);
                }
            }
        }
    }
    
    private void drawFireParticles(Graphics2D g2d, float centerX, float centerY, float alpha) {
        // Partículas pequeñas ardientes alrededor de la estela
        for (int p = 0; p < 3; p++) {
            float particleX = centerX + (random.nextFloat() - 0.5f) * width;
            float particleY = centerY + (random.nextFloat() - 0.5f) * height;
            int particleSize = 2 + random.nextInt(3);
            
            g2d.setColor(new Color(255, 150 + random.nextInt(105), random.nextInt(100), (int)(alpha * 150)));
            g2d.fillOval((int)particleX, (int)particleY, particleSize, particleSize);
        }
    }
    
    public boolean isExpired() {
        return System.currentTimeMillis() - creationTime >= LIFETIME;
    }
    
    public boolean isOffScreen(int gameWidth, int gameHeight) {
        return x < -width - 50 || x > gameWidth + 50 || y > gameHeight + 50;
    }
    
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, width, height);
    }
    
    public float getX() { return x; }
    public float getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
} 