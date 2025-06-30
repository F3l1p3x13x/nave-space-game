import java.awt.*;

public class SpaceShip {
    private int x, y;
    private int width, height;
    private int speed;
    private int gameWidth, gameHeight;
    
    public SpaceShip(int gameWidth, int gameHeight) {
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        this.width = 40;
        this.height = 30;
        this.speed = 5;
        this.x = 50; // Posición inicial cerca del borde izquierdo
        this.y = gameHeight / 2 - height / 2; // Centrado verticalmente
    }
    
    public void moveUp() {
        if (y > 0) {
            y -= speed;
        }
    }
    
    public void moveDown() {
        if (y < gameHeight - height) {
            y += speed;
        }
    }
    
    public void moveLeft() {
        if (x > 0) {
            x -= speed;
        }
    }
    
    public void moveRight() {
        if (x < gameWidth - width) {
            x += speed;
        }
    }
    
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Dibujar sombra de la nave (efecto 3D)
        g2d.setColor(new Color(0, 0, 0, 100));
        int[] shadowXPoints = {x + 3, x + width + 3, x + 3};
        int[] shadowYPoints = {y + height/2 + 3, y + 3, y + height + 3};
        g2d.fillPolygon(shadowXPoints, shadowYPoints, 3);
        
        // Crear gradiente 3D para el cuerpo de la nave
        GradientPaint gradient = new GradientPaint(
            x, y, new Color(0, 255, 255, 255), // Cyan brillante arriba
            x, y + height, new Color(0, 150, 200, 255) // Azul más oscuro abajo
        );
        g2d.setPaint(gradient);
        
        // Dibujar la nave como un triángulo con gradiente
        int[] xPoints = {x, x + width, x};
        int[] yPoints = {y + height/2, y, y + height};
        g2d.fillPolygon(xPoints, yPoints, 3);
        
        // Añadir borde metálico
        g2d.setColor(new Color(200, 200, 255));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawPolygon(xPoints, yPoints, 3);
        
        // Cabina con efecto de cristal 3D
        GradientPaint glassGradient = new GradientPaint(
            x + 5, y + height/2 - 3, new Color(255, 255, 255, 180),
            x + 11, y + height/2 + 3, new Color(150, 200, 255, 100)
        );
        g2d.setPaint(glassGradient);
        g2d.fillOval(x + 5, y + height/2 - 3, 6, 6);
        
        // Reflejo en la cabina
        g2d.setColor(new Color(255, 255, 255, 200));
        g2d.fillOval(x + 6, y + height/2 - 2, 2, 2);
        
        // Propulsores con efecto de llama 3D
        drawThruster(g2d, x - 8, y + height/4);
        drawThruster(g2d, x - 8, y + 3*height/4);
        
        g2d.dispose();
    }
    
    private void drawThruster(Graphics2D g2d, int thrusterX, int thrusterY) {
        // Llama del propulsor con gradiente radial
        RadialGradientPaint thrusterFlame = new RadialGradientPaint(
            thrusterX - 4, thrusterY, 6,
            new float[]{0.0f, 0.5f, 1.0f},
            new Color[]{
                new Color(255, 255, 100, 255), // Amarillo brillante centro
                new Color(255, 100, 0, 200),   // Naranja medio
                new Color(255, 0, 0, 100)      // Rojo exterior
            }
        );
        g2d.setPaint(thrusterFlame);
        g2d.fillOval(thrusterX - 6, thrusterY - 2, 8, 4);
        
        // Núcleo blanco brillante
        g2d.setColor(new Color(255, 255, 255, 255));
        g2d.fillOval(thrusterX - 2, thrusterY - 1, 3, 2);
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    
    public void resetPosition() {
        this.x = 50; // Posición inicial cerca del borde izquierdo
        this.y = gameHeight / 2 - height / 2; // Centrado verticalmente
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
} 