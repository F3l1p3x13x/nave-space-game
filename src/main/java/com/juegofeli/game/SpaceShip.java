package com.juegofeli.game;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class SpaceShip {
    private int x, y;
    private int width, height;
    private int speed;
    private int gameWidth, gameHeight;
    private BufferedImage spaceShipImage;
    private boolean useImage;
    
    public SpaceShip(int gameWidth, int gameHeight) {
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        this.width = 160;  // Cuadruplicado para mejor visibilidad (era 40, luego 80)
        this.height = 120; // Cuadruplicado para mejor visibilidad (era 30, luego 60)
        this.speed = 5;
        this.x = 80; // Posición inicial ajustada para nave más grande
        this.y = gameHeight / 2 - height / 2; // Centrado verticalmente
        
        // Intentar cargar la imagen de la nave
        loadSpaceShipImage();
    }
    
    private void loadSpaceShipImage() {
        try {
            // Intentar cargar desde resources primero
            java.io.InputStream imageStream = getClass().getResourceAsStream("/images/nave_space_ship.png");
            if (imageStream != null) {
                BufferedImage originalImage = ImageIO.read(imageStream);
                imageStream.close();
                
                if (originalImage != null) {
                    // Procesar la imagen para manejar transparencia correctamente
                    spaceShipImage = processTransparency(originalImage);
                    useImage = true;
                    
                    // Redimensionar la nave según la imagen si es necesario
                    double imageRatio = (double) spaceShipImage.getWidth() / spaceShipImage.getHeight();
                    this.height = 160; // Altura cuadruplicada para mejor visibilidad (era 40, luego 80)
                    this.width = (int) (height * imageRatio);
                    
                    System.out.println("✅ Imagen nave_space_ship.png cargada exitosamente desde resources");
                    System.out.println("📐 Dimensiones: " + width + "x" + height + " píxeles");
                    System.out.println("🔍 Transparencia procesada correctamente");
                    return;
                }
            }
            
            // Fallback: intentar cargar desde archivo local (para compatibilidad)
            File imageFile = new File("src/main/resources/images/nave_space_ship.png");
            if (imageFile.exists()) {
                BufferedImage originalImage = ImageIO.read(imageFile);
                
                if (originalImage != null) {
                    // Procesar la imagen para manejar transparencia correctamente
                    spaceShipImage = processTransparency(originalImage);
                    useImage = true;
                    
                    // Redimensionar la nave según la imagen si es necesario
                    double imageRatio = (double) spaceShipImage.getWidth() / spaceShipImage.getHeight();
                    this.height = 160; // Altura cuadruplicada para mejor visibilidad (era 40, luego 80)
                    this.width = (int) (height * imageRatio);
                    
                    System.out.println("✅ Imagen nave_space_ship.png cargada exitosamente");
                    System.out.println("📐 Dimensiones: " + width + "x" + height + " píxeles");
                    System.out.println("🔍 Transparencia procesada correctamente");
                }
            } else {
                useImage = false;
                System.out.println("⚠️ Archivo nave_space_ship.png no encontrado");
                System.out.println("🎨 Usando gráficos 3D programáticos como fallback");
            }
        } catch (IOException e) {
            useImage = false;
            System.err.println("❌ Error cargando nave_space_ship.png: " + e.getMessage());
            System.out.println("🎨 Usando gráficos 3D programáticos como fallback");
        }
    }
    
    private BufferedImage processTransparency(BufferedImage originalImage) {
        // Crear una nueva imagen con transparencia correcta
        BufferedImage processedImage = new BufferedImage(
            originalImage.getWidth(), 
            originalImage.getHeight(), 
            BufferedImage.TYPE_INT_ARGB
        );
        
        Graphics2D g2d = processedImage.createGraphics();
        g2d.setComposite(AlphaComposite.Src);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Procesar píxel por píxel para eliminar fondos no deseados
        for (int y = 0; y < originalImage.getHeight(); y++) {
            for (int x = 0; x < originalImage.getWidth(); x++) {
                int pixel = originalImage.getRGB(x, y);
                
                // Extraer componentes ARGB
                int alpha = (pixel >> 24) & 0xFF;
                int red = (pixel >> 16) & 0xFF;
                int green = (pixel >> 8) & 0xFF;
                int blue = pixel & 0xFF;
                
                // Filtrar píxeles de fondo/transparentes
                if (alpha < 50) { // Píxeles muy transparentes
                    // Hacer completamente transparente
                    processedImage.setRGB(x, y, 0x00000000);
                } else if (isBackgroundPixel(red, green, blue)) {
                    // Detectar y eliminar fondos blancos, grises o cuadros de transparencia
                    processedImage.setRGB(x, y, 0x00000000);
                } else {
                    // Mantener el píxel original pero asegurar alpha completo
                    int newAlpha = Math.max(alpha, 255);
                    int newPixel = (newAlpha << 24) | (red << 16) | (green << 8) | blue;
                    processedImage.setRGB(x, y, newPixel);
                }
            }
        }
        
        g2d.dispose();
        return processedImage;
    }
    
    private boolean isBackgroundPixel(int red, int green, int blue) {
        // Detectar píxeles de fondo comunes (blancos, grises, pattern de transparencia)
        
        // Blancos y grises claros
        if (red > 240 && green > 240 && blue > 240) return true;
        
        // Patrón de cuadros de transparencia (gris claro/blanco alternado)
        if ((red == green && green == blue) && (red > 200 || red < 60)) return true;
        
        // Colores muy similares entre sí (posible fondo uniforme)
        int diff1 = Math.abs(red - green);
        int diff2 = Math.abs(green - blue);
        int diff3 = Math.abs(red - blue);
        if (diff1 < 10 && diff2 < 10 && diff3 < 10 && red > 220) return true;
        
        return false;
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
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        
        if (useImage && spaceShipImage != null) {
            // Usar imagen PNG externa
            drawImageSpaceShip(g2d);
        } else {
            // Usar gráficos 3D programáticos como fallback
            drawProgrammaticSpaceShip(g2d);
        }
        
        g2d.dispose();
    }
    
    private void drawImageSpaceShip(Graphics2D g2d) {
        // Configurar renderizado óptimo para transparencia
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        
        // Dibujar sombra sutil ajustada al nuevo tamaño
        g2d.setColor(new Color(0, 0, 0, 40));
        g2d.fillOval(x + 4, y + 4, width, height);
        
        // Dibujar la imagen procesada con transparencia perfecta
        // La imagen ya tiene la transparencia procesada correctamente
        g2d.drawImage(spaceShipImage, x, y, width, height, null);
        
        // Efectos mínimos y opcionales - comentar estas líneas si no los quieres
        // drawImageEffects(g2d);
    }
    
    private void drawImageEffects(Graphics2D g2d) {
        // Brillo/halo ajustado al nuevo tamaño mayor
        g2d.setColor(new Color(0, 150, 255, 20));
        g2d.fillOval(x - 8, y - 8, width + 16, height + 16);
        
        // Propulsores ajustados al nuevo tamaño mayor
        drawImageThruster(g2d, x - 20, y + height/3);
        drawImageThruster(g2d, x - 20, y + 2*height/3);
        
        // Efectos de energía/escudo ocasionales ajustados al nuevo tamaño
        if (System.currentTimeMillis() % 3000 < 50) { // Parpadeo más raro
            g2d.setColor(new Color(255, 255, 255, 80));
            g2d.setStroke(new BasicStroke(3));
            g2d.drawOval(x - 4, y - 4, width + 8, height + 8);
        }
    }
    
    private void drawImageThruster(Graphics2D g2d, int thrusterX, int thrusterY) {
        // Llama del propulsor aumentada para nave más grande
        RadialGradientPaint thrusterFlame = new RadialGradientPaint(
            thrusterX - 12, thrusterY, 16,
            new float[]{0.0f, 0.6f, 1.0f},
            new Color[]{
                new Color(255, 255, 150, 255), // Amarillo brillante centro
                new Color(255, 120, 0, 180),   // Naranja medio
                new Color(255, 60, 0, 80)      // Rojo exterior
            }
        );
        g2d.setPaint(thrusterFlame);
        g2d.fillOval(thrusterX - 16, thrusterY - 6, 24, 12);
        
        // Núcleo blanco brillante más grande
        g2d.setColor(new Color(255, 255, 255, 255));
        g2d.fillOval(thrusterX - 4, thrusterY - 1, 8, 4);
    }
    
    private void drawProgrammaticSpaceShip(Graphics2D g2d) {
        // Código original de gráficos 3D programáticos
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
        
        // Cabina con efecto de cristal 3D (ajustada al nuevo tamaño mayor)
        GradientPaint glassGradient = new GradientPaint(
            x + 20, y + height/2 - 12, new Color(255, 255, 255, 180),
            x + 44, y + height/2 + 12, new Color(150, 200, 255, 100)
        );
        g2d.setPaint(glassGradient);
        g2d.fillOval(x + 20, y + height/2 - 12, 24, 24);
        
        // Reflejo en la cabina más grande
        g2d.setColor(new Color(255, 255, 255, 200));
        g2d.fillOval(x + 24, y + height/2 - 8, 8, 8);
        
        // Propulsores con efecto de llama 3D (ajustados al nuevo tamaño mayor)
        drawThruster(g2d, x - 24, y + height/4);
        drawThruster(g2d, x - 24, y + 3*height/4);
    }
    
    private void drawThruster(Graphics2D g2d, int thrusterX, int thrusterY) {
        // Llama del propulsor con gradiente radial (aumentada para el nuevo tamaño)
        RadialGradientPaint thrusterFlame = new RadialGradientPaint(
            thrusterX - 12, thrusterY, 18,
            new float[]{0.0f, 0.5f, 1.0f},
            new Color[]{
                new Color(255, 255, 100, 255), // Amarillo brillante centro
                new Color(255, 100, 0, 200),   // Naranja medio
                new Color(255, 0, 0, 100)      // Rojo exterior
            }
        );
        g2d.setPaint(thrusterFlame);
        g2d.fillOval(thrusterX - 18, thrusterY - 6, 24, 12);
        
        // Núcleo blanco brillante más grande
        g2d.setColor(new Color(255, 255, 255, 255));
        g2d.fillOval(thrusterX - 6, thrusterY - 3, 9, 6);
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    
    public void resetPosition() {
        this.x = 80; // Posición inicial ajustada para nave más grande
        this.y = gameHeight / 2 - height / 2; // Centrado verticalmente
        
        // Recargar imagen si es necesario (en caso de que se haya agregado después)
        if (!useImage) {
            loadSpaceShipImage();
        }
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
} 