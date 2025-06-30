package com.juegofeli.game;

import java.awt.*;

public class Bullet {
    private int x, y;
    private int width, height;
    private int speed;
    private int gameWidth;
    private boolean active;
    
    public Bullet(int startX, int startY, int gameWidth) {
        this.x = startX;
        this.y = startY;
        this.gameWidth = gameWidth;
        this.width = 8;
        this.height = 4;
        this.speed = 12; // Rápido para que sea efectivo
        this.active = true;
    }
    
    public void update() {
        if (active) {
            x += speed;
            
            // Desactivar si sale de la pantalla
            if (x > gameWidth) {
                active = false;
            }
        }
    }
    
    public void draw(Graphics g) {
        if (active) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Dibujar proyectil con efectos 3D
            drawLaserBullet(g2d);
            
            g2d.dispose();
        }
    }
    
    private void drawLaserBullet(Graphics2D g2d) {
        // Núcleo del láser (blanco brillante)
        g2d.setColor(new Color(255, 255, 255, 255));
        g2d.fillOval(x, y + 1, width, height - 2);
        
        // Halo exterior (azul cyan)
        g2d.setColor(new Color(0, 255, 255, 150));
        g2d.fillOval(x - 1, y, width + 2, height);
        
        // Efecto de energía (más azul y transparente)
        g2d.setColor(new Color(0, 150, 255, 80));
        g2d.fillOval(x - 2, y - 1, width + 4, height + 2);
        
        // Destello central
        g2d.setColor(new Color(255, 255, 255, 255));
        g2d.fillOval(x + 2, y + 1, width - 4, height - 2);
        
        // Efecto de movimiento (líneas de velocidad)
        g2d.setColor(new Color(0, 255, 255, 100));
        g2d.setStroke(new BasicStroke(1));
        g2d.drawLine(x - 3, y + height/2, x - 8, y + height/2);
        g2d.drawLine(x - 2, y + height/2 - 1, x - 6, y + height/2 - 1);
        g2d.drawLine(x - 2, y + height/2 + 1, x - 6, y + height/2 + 1);
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
} 