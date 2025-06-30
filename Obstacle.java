import java.awt.*;
import java.util.Random;

public class Obstacle {
    private int x, y;
    private int width, height;
    private int speed;
    private ObstacleType type;
    private Color color;
    private Random random;
    
    public enum ObstacleType {
        ASTEROID, PLANET
    }
    
    public Obstacle(int gameWidth, int gameHeight, int difficultyLevel) {
        this.random = new Random();
        this.x = gameWidth + random.nextInt(200); // Aparece fuera de la pantalla
        this.y = random.nextInt(gameHeight - 100);
        
        // Velocidad aumenta con la dificultad
        int baseSpeed = 2 + random.nextInt(3); // Velocidad base entre 2 y 4
        this.speed = baseSpeed + (difficultyLevel - 1) / 2; // Aumenta cada 2 niveles
        
        // Decidir aleatoriamente si es asteroide o planeta
        this.type = random.nextBoolean() ? ObstacleType.ASTEROID : ObstacleType.PLANET;
        
        // Tamaño aumenta ligeramente con la dificultad
        int sizeBonus = (difficultyLevel - 1) * 2;
        
        if (type == ObstacleType.ASTEROID) {
            this.width = 30 + random.nextInt(20) + sizeBonus;
            this.height = 30 + random.nextInt(20) + sizeBonus;
            this.color = new Color(139, 69, 19); // Marrón para asteroides
        } else {
            this.width = 50 + random.nextInt(30) + sizeBonus;
            this.height = 50 + random.nextInt(30) + sizeBonus;
            // Colores variados para planetas
            Color[] planetColors = {
                new Color(255, 69, 0),    // Rojo-naranja
                new Color(0, 128, 255),   // Azul
                new Color(255, 215, 0),   // Dorado
                new Color(138, 43, 226),  // Púrpura
                new Color(34, 139, 34)    // Verde
            };
            this.color = planetColors[random.nextInt(planetColors.length)];
        }
    }
    
    public void update() {
        x -= speed;
    }
    
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (type == ObstacleType.ASTEROID) {
            drawAsteroid3D(g2d);
        } else {
            drawPlanet3D(g2d);
        }
        
        g2d.dispose();
    }
    
    private void drawAsteroid3D(Graphics2D g2d) {
        // Sombra del asteroide
        g2d.setColor(new Color(0, 0, 0, 80));
        g2d.fillOval(x + 4, y + 4, width, height);
        
        // Gradiente radial para efecto 3D
        RadialGradientPaint asteroidGradient = new RadialGradientPaint(
            x + width/3, y + height/3, Math.max(width, height)/2,
            new float[]{0.0f, 0.7f, 1.0f},
            new Color[]{
                color.brighter().brighter(), // Centro iluminado
                color,                       // Color base
                color.darker().darker()      // Bordes oscuros
            }
        );
        g2d.setPaint(asteroidGradient);
        g2d.fillOval(x, y, width, height);
        
        // Textura rugosa con cráteres 3D
        drawCrater3D(g2d, x + width/4, y + height/4, width/6, height/6);
        drawCrater3D(g2d, x + width/2, y + height/3, width/8, height/8);
        drawCrater3D(g2d, x + 2*width/3, y + 2*height/3, width/10, height/10);
        
        // Borde con iluminación
        g2d.setColor(color.brighter());
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawArc(x, y, width/3, height/3, 45, 90); // Reflejo superior izquierdo
    }
    
    private void drawCrater3D(Graphics2D g2d, int craterX, int craterY, int craterW, int craterH) {
        // Cráter con gradiente cóncavo
        RadialGradientPaint craterGradient = new RadialGradientPaint(
            craterX + craterW/2, craterY + craterH/2, Math.max(craterW, craterH)/2,
            new float[]{0.0f, 1.0f},
            new Color[]{
                color.darker().darker().darker(), // Centro muy oscuro
                color.darker()                    // Borde del cráter
            }
        );
        g2d.setPaint(craterGradient);
        g2d.fillOval(craterX, craterY, craterW, craterH);
        
        // Borde iluminado del cráter
        g2d.setColor(color.brighter());
        g2d.setStroke(new BasicStroke(1));
        g2d.drawArc(craterX-1, craterY-1, craterW/2, craterH/2, 45, 180);
    }
    
    private void drawPlanet3D(Graphics2D g2d) {
        // Sombra del planeta
        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.fillOval(x + 5, y + 5, width, height);
        
        // Gradiente radial principal para efecto esférico 3D
        RadialGradientPaint planetGradient = new RadialGradientPaint(
            x + width/3, y + height/3, Math.max(width, height)/2,
            new float[]{0.0f, 0.4f, 0.8f, 1.0f},
            new Color[]{
                new Color(Math.min(255, color.getRed() + 80), 
                         Math.min(255, color.getGreen() + 80), 
                         Math.min(255, color.getBlue() + 80), 255), // Centro muy brillante
                color.brighter(),                                   // Zona iluminada
                color,                                             // Color base
                color.darker().darker()                            // Terminador (zona oscura)
            }
        );
        g2d.setPaint(planetGradient);
        g2d.fillOval(x, y, width, height);
        
        // Características de superficie (continentes/océanos)
        drawPlanetFeatures(g2d);
        
        // Anillos planetarios con perspectiva 3D
        if (random.nextInt(3) == 0) {
            drawPlanetaryRings3D(g2d);
        }
        
        // Reflejo especular (brillo)
        g2d.setColor(new Color(255, 255, 255, 150));
        g2d.fillOval(x + width/4, y + height/5, width/8, height/8);
        
        // Atmósfera (halo sutil)
        g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
        g2d.fillOval(x - 3, y - 3, width + 6, height + 6);
    }
    
    private void drawPlanetFeatures(Graphics2D g2d) {
        // Características geográficas con transparencia
        Color featureColor = new Color(
            Math.max(0, color.getRed() - 40),
            Math.max(0, color.getGreen() - 20),
            Math.max(0, color.getBlue() - 30),
            120
        );
        g2d.setColor(featureColor);
        
        // Continentes/manchas
        g2d.fillOval(x + width/6, y + height/4, width/3, height/4);
        g2d.fillOval(x + width/2, y + height/6, width/4, height/3);
        g2d.fillOval(x + 2*width/3, y + 2*height/3, width/5, height/6);
    }
    
    private void drawPlanetaryRings3D(Graphics2D g2d) {
        // Anillos con perspectiva elíptica
        Color ringColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 100);
        g2d.setColor(ringColor);
        g2d.setStroke(new BasicStroke(3));
        
        // Anillo principal (elipse para perspectiva)
        g2d.drawOval(x - 12, y + height/3, width + 24, height/3);
        
        // Anillo secundario
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 60));
        g2d.drawOval(x - 18, y + height/4, width + 36, height/2);
        
        // Parte trasera del anillo (más tenue)
        g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 40));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawArc(x - 12, y + height/3, width + 24, height/3, 0, 180);
    }
    
    public boolean isOffScreen() {
        return x + width < 0;
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
    public ObstacleType getType() { return type; }
} 