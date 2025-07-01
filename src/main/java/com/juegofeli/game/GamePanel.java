package com.juegofeli.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private static final int PANEL_WIDTH = 800;
    private static final int PANEL_HEIGHT = 600;
    
    // Estados del juego
    private enum GameState {
        INTRO_SCREEN,
        PLAYING,
        GAME_OVER
    }
    
    private GameState currentState;
    
    private Timer gameTimer;
    private SpaceShip spaceShip;
    private ArrayList<Obstacle> obstacles;
    private Random random;
    
    // Control de juego
    private boolean gameRunning;
    private long startTime;
    private long currentTime;
    private int score;
    
    // Control de teclas
    private boolean upPressed, downPressed, leftPressed, rightPressed;
    
    // Generación de obstáculos
    private int obstacleSpawnTimer;
    private int obstacleSpawnDelay;
    
    // Sistema de dificultad
    private int difficultyLevel;
    private long lastDifficultyIncrease;
    
    // Notificación de nivel
    private boolean showLevelUpMessage;
    private long levelUpMessageTime;
    
    // Fondo de estrellas
    private ArrayList<Star> stars;
    
    // Reproductor de música
    private MusicPlayer musicPlayer;
    
    // Sistema de meteoritos (desde nivel 6)
    private ArrayList<Meteorite> meteorites;
    private int meteoriteSpawnTimer;
    private Random meteoriteRandom;
    
    // Sistema de disparos
    private ArrayList<Bullet> bullets;
    private boolean spacePressed;
    private int shootCooldown;
    private static final int SHOOT_COOLDOWN_TIME = 8; // Cooldown entre disparos (8 frames = ~133ms)
    
    // Imágenes para pantalla inicial
    private BufferedImage felipeImage;
    private BufferedImage naveImage;
    
    public GamePanel() {
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);
        
        // Establecer estado inicial
        currentState = GameState.INTRO_SCREEN;
        gameRunning = false; // Importante: No iniciar el juego automáticamente
        

        
        random = new Random();
        obstacles = new ArrayList<>();
        stars = new ArrayList<>();
        meteorites = new ArrayList<>();
        meteoriteRandom = new Random();
        bullets = new ArrayList<>();
        
        // Cargar imágenes para pantalla inicial
        loadIntroImages();
        
        // Crear estrellas de fondo
        createStars();
        
        // Inicializar nave espacial
        spaceShip = new SpaceShip(PANEL_WIDTH, PANEL_HEIGHT);
        
        // Inicializar reproductor de música
        musicPlayer = new MusicPlayer();
        
        // Iniciar música épica de introducción
        if (musicPlayer != null) {
            musicPlayer.playIntroMusic();
        }
        
        // Configurar temporizador
        gameTimer = new Timer(16, this); // ~60 FPS
        gameTimer.start(); // Iniciar timer para mostrar pantalla inicial
        obstacleSpawnDelay = 80; // Cada ~1.3 segundos
        

    }
    
    private void createStars() {
        for (int i = 0; i < 100; i++) {
            stars.add(new Star(random.nextInt(PANEL_WIDTH), random.nextInt(PANEL_HEIGHT)));
        }
    }
    
    private void loadIntroImages() {
        try {
            // Cargar SOLO imagen de Felipe (astronauta)
            java.io.InputStream felipeStream = getClass().getResourceAsStream("/images/felipe.png");
            if (felipeStream != null) {
                felipeImage = ImageIO.read(felipeStream);
                felipeStream.close();
                System.out.println("✅ Imagen felipe.png cargada exitosamente");
                System.out.println("📐 Felipe dimensiones: " + felipeImage.getWidth() + "x" + felipeImage.getHeight() + " píxeles");
            } else {
                System.out.println("⚠️ No se pudo cargar felipe.png");
            }
        } catch (IOException e) {
            System.err.println("❌ Error cargando imagen de Felipe: " + e.getMessage());
        }
    }
    
    /**
     * Procesa una imagen para hacer transparentes los píxeles de fondo
     * Detecta automáticamente el color de fondo más común en los bordes
     */
    private BufferedImage processImageTransparency(BufferedImage original) {
        if (original == null) return null;
        
        int width = original.getWidth();
        int height = original.getHeight();
        
        // Crear nueva imagen con canal alfa
        BufferedImage processedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        
        // Detectar color de fondo basado en las esquinas y bordes
        Color backgroundColor = detectBackgroundColor(original);
        int bgRGB = backgroundColor.getRGB() & 0x00FFFFFF; // Sin canal alfa
        
        // Umbral de tolerancia para colores similares al fondo
        int tolerance = 30;
        
        // Procesar cada píxel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixelRGB = original.getRGB(x, y);
                int pixelColor = pixelRGB & 0x00FFFFFF; // Sin canal alfa
                
                // Calcular diferencia de color
                int rDiff = Math.abs(((pixelColor >> 16) & 0xFF) - ((bgRGB >> 16) & 0xFF));
                int gDiff = Math.abs(((pixelColor >> 8) & 0xFF) - ((bgRGB >> 8) & 0xFF));
                int bDiff = Math.abs((pixelColor & 0xFF) - (bgRGB & 0xFF));
                int totalDiff = rDiff + gDiff + bDiff;
                
                if (totalDiff <= tolerance) {
                    // Hacer transparente
                    processedImage.setRGB(x, y, 0x00000000);
                } else {
                    // Mantener píxel original
                    processedImage.setRGB(x, y, pixelRGB);
                }
            }
        }
        
        return processedImage;
    }
    
    /**
     * Detecta el color de fondo más común en los bordes de la imagen
     */
    private Color detectBackgroundColor(BufferedImage image) {
        if (image == null) return Color.WHITE;
        
        java.util.Map<Integer, Integer> colorCount = new java.util.HashMap<>();
        int width = image.getWidth();
        int height = image.getHeight();
        
        // Muestrear píxeles de los bordes
        // Borde superior
        for (int x = 0; x < width; x++) {
            int rgb = image.getRGB(x, 0) & 0x00FFFFFF;
            colorCount.put(rgb, colorCount.getOrDefault(rgb, 0) + 1);
        }
        
        // Borde inferior
        for (int x = 0; x < width; x++) {
            int rgb = image.getRGB(x, height - 1) & 0x00FFFFFF;
            colorCount.put(rgb, colorCount.getOrDefault(rgb, 0) + 1);
        }
        
        // Borde izquierdo
        for (int y = 0; y < height; y++) {
            int rgb = image.getRGB(0, y) & 0x00FFFFFF;
            colorCount.put(rgb, colorCount.getOrDefault(rgb, 0) + 1);
        }
        
        // Borde derecho
        for (int y = 0; y < height; y++) {
            int rgb = image.getRGB(width - 1, y) & 0x00FFFFFF;
            colorCount.put(rgb, colorCount.getOrDefault(rgb, 0) + 1);
        }
        
        // Encontrar el color más frecuente
        int mostCommonColor = 0xFFFFFF; // Blanco por defecto
        int maxCount = 0;
        
        for (java.util.Map.Entry<Integer, Integer> entry : colorCount.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostCommonColor = entry.getKey();
            }
        }
        
        return new Color(mostCommonColor);
    }
    
    public void startGame() {

        currentState = GameState.PLAYING;
        gameRunning = true;
        startTime = System.currentTimeMillis();
        lastDifficultyIncrease = startTime;
        score = 0;
        difficultyLevel = 1;
        showLevelUpMessage = false;
        obstacles.clear();
        meteorites.clear();
        bullets.clear();
        obstacleSpawnTimer = 0;
        meteoriteSpawnTimer = 0;
        shootCooldown = 0;
        spacePressed = false;
        
        // ========== CORRECIÓN DE BUG DE REINICIO ==========
        // Resetear TODAS las variables modificadas durante el juego a sus valores iniciales
        
        // IMPORTANTE: Resetear la frecuencia de spawn al valor inicial
        obstacleSpawnDelay = 80; // Cada ~1.3 segundos (valor inicial)
        
        // Resetear posición de la nave al centro inicial  
        spaceShip.resetPosition();
        
        // Resetear estado de teclas presionadas
        upPressed = downPressed = leftPressed = rightPressed = false;
        
        // Cambiar a música del juego
        if (musicPlayer != null) {
            musicPlayer.stopMusic();
            musicPlayer.playGameMusic();
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (currentState) {
            case INTRO_SCREEN:
                // Solo actualizar estrellas de fondo en pantalla inicial
                for (Star star : stars) {
                    star.update();
                    if (star.getX() < 0) {
                        star.reset(PANEL_WIDTH);
                    }
                }
                break;
            case PLAYING:
                if (gameRunning) {
                    update();
                }
                break;
            case GAME_OVER:
                // Actualizar estrellas de fondo en game over
                for (Star star : stars) {
                    star.update();
                    if (star.getX() < 0) {
                        star.reset(PANEL_WIDTH);
                    }
                }
                break;
        }
        repaint();
    }
    
    private void update() {
        currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - startTime;
        
        // Actualizar puntuación actual (tiempo sobrevivido)
        score = (int) (elapsedTime / 1000);
        
        // Verificar si es hora de aumentar la dificultad (cada 30 segundos)
        if (currentTime - lastDifficultyIncrease >= 30000) {
            increaseDifficulty();
            lastDifficultyIncrease = currentTime;
        }
        
        // Ocultar mensaje de nivel después de 2 segundos
        if (showLevelUpMessage && (currentTime - levelUpMessageTime) > 2000) {
            showLevelUpMessage = false;
        }
        
        // Mover nave según teclas presionadas
        if (upPressed) spaceShip.moveUp();
        if (downPressed) spaceShip.moveDown();
        if (leftPressed) spaceShip.moveLeft();
        if (rightPressed) spaceShip.moveRight();
        
        // Sistema de disparos
        handleShooting();
        updateBullets();
        
        // Generar obstáculos
        obstacleSpawnTimer++;
        if (obstacleSpawnTimer >= obstacleSpawnDelay) {
            obstacles.add(new Obstacle(PANEL_WIDTH, PANEL_HEIGHT, difficultyLevel));
            obstacleSpawnTimer = 0;
        }
        
        // Generar meteoritos desde nivel 6
        if (difficultyLevel >= 6) {
            meteoriteSpawnTimer++;
            // Frecuencia aleatoria de meteoritos (más frecuente en niveles más altos)
            int meteoriteSpawnChance = 120 - (difficultyLevel - 6) * 10; // Comienza cada ~2 segundos, aumenta frecuencia
            if (meteoriteSpawnChance < 40) meteoriteSpawnChance = 40; // Mínimo cada ~0.7 segundos
            
            if (meteoriteSpawnTimer >= meteoriteSpawnChance && meteoriteRandom.nextFloat() < 0.3f) {
                meteorites.add(new Meteorite(PANEL_WIDTH, PANEL_HEIGHT));
                meteoriteSpawnTimer = 0;
            }
        }
        
        // Actualizar obstáculos
        Iterator<Obstacle> obstacleIterator = obstacles.iterator();
        while (obstacleIterator.hasNext()) {
            Obstacle obstacle = obstacleIterator.next();
            obstacle.update();
            
            // Remover obstáculos que salen de pantalla
            if (obstacle.isOffScreen()) {
                obstacleIterator.remove();
            }
            // Verificar colisiones
            else if (spaceShip.getBounds().intersects(obstacle.getBounds())) {
                gameRunning = false;
                currentState = GameState.GAME_OVER;
                
                // Detener música al terminar el juego
                if (musicPlayer != null) {
                    musicPlayer.stopMusic();
                }
                return;
            }
        }
        
        // Actualizar meteoritos
        Iterator<Meteorite> meteoriteIterator = meteorites.iterator();
        while (meteoriteIterator.hasNext()) {
            Meteorite meteorite = meteoriteIterator.next();
            meteorite.update();
            
            // Remover meteoritos expirados o fuera de pantalla
            if (meteorite.isExpired() || meteorite.isOffScreen(PANEL_WIDTH, PANEL_HEIGHT)) {
                meteoriteIterator.remove();
            }
            // Verificar colisiones con meteoritos
            else if (spaceShip.getBounds().intersects(meteorite.getBounds())) {
                gameRunning = false;
                currentState = GameState.GAME_OVER;
                
                // Detener música al terminar el juego
                if (musicPlayer != null) {
                    musicPlayer.stopMusic();
                }
                return;
            }
        }
        
        // Actualizar estrellas de fondo
        for (Star star : stars) {
            star.update();
            if (star.getX() < 0) {
                star.reset(PANEL_WIDTH);
            }
        }
    }
    
    private void increaseDifficulty() {
        difficultyLevel++;
        
        // Mostrar mensaje de nivel up
        showLevelUpMessage = true;
        levelUpMessageTime = System.currentTimeMillis();
        
        // Reducir tiempo entre spawn de obstáculos
        if (obstacleSpawnDelay > 25) {
            obstacleSpawnDelay -= 8; // Reducción más agresiva cada nivel
        } else if (obstacleSpawnDelay > 15) {
            obstacleSpawnDelay -= 3;
        }
        
        // Asegurar que no sea demasiado rápido
        if (obstacleSpawnDelay < 15) {
            obstacleSpawnDelay = 15;
        }
    }
    
    private void handleShooting() {
        // Reducir cooldown
        if (shootCooldown > 0) {
            shootCooldown--;
        }
        
        // Crear nueva bala si se presiona espacio y no hay cooldown
        if (spacePressed && shootCooldown <= 0) {
            // Crear bala desde la parte delantera de la nave
            int bulletX = spaceShip.getX() + spaceShip.getWidth();
            int bulletY = spaceShip.getY() + spaceShip.getHeight() / 2 - 2; // Centrada verticalmente
            
            bullets.add(new Bullet(bulletX, bulletY, PANEL_WIDTH));
            shootCooldown = SHOOT_COOLDOWN_TIME;
        }
    }
    
    private void updateBullets() {
        // Actualizar todas las balas
        Iterator<Bullet> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            bullet.update();
            
            // Remover balas inactivas
            if (!bullet.isActive()) {
                bulletIterator.remove();
                continue;
            }
            
            // Verificar colisiones con obstáculos
            Iterator<Obstacle> obstacleIterator = obstacles.iterator();
            while (obstacleIterator.hasNext()) {
                Obstacle obstacle = obstacleIterator.next();
                if (bullet.getBounds().intersects(obstacle.getBounds())) {
                    // Destruir obstáculo y bala
                    bulletIterator.remove();
                    obstacleIterator.remove();
                    score += 10; // Bonus por destruir obstáculo
                    break;
                }
            }
            
            // Verificar colisiones con meteoritos
            if (bullet.isActive()) {
                Iterator<Meteorite> meteoriteIterator = meteorites.iterator();
                while (meteoriteIterator.hasNext()) {
                    Meteorite meteorite = meteoriteIterator.next();
                    if (bullet.getBounds().intersects(meteorite.getBounds())) {
                        // Destruir meteorito y bala
                        bulletIterator.remove();
                        meteoriteIterator.remove();
                        score += 20; // Bonus mayor por destruir meteorito
                        break;
                    }
                }
            }
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    
    private void draw(Graphics g) {
        switch (currentState) {
            case INTRO_SCREEN:
                drawIntroScreen(g);
                break;
            case PLAYING:
                drawGameScreen(g);
                break;
            case GAME_OVER:
                drawGameOverScreen(g);
                break;
        }
    }
    
    private void drawGameScreen(Graphics g) {
        if (gameRunning) {
            // Dibujar fondo espacial con gradiente 3D
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            drawSpaceBackground(g2d);
            
            // Dibujar estrellas de fondo con efectos 3D
            for (Star star : stars) {
                star.draw(g2d);
            }
            g2d.dispose();
            
            // Dibujar nave espacial
            spaceShip.draw(g);
            
            // Dibujar obstáculos
            for (Obstacle obstacle : obstacles) {
                obstacle.draw(g);
            }
            
            // Dibujar meteoritos
            for (Meteorite meteorite : meteorites) {
                meteorite.draw(g);
            }
            
            // Dibujar balas
            for (Bullet bullet : bullets) {
                bullet.draw(g);
            }
            
            // Mostrar tiempo/puntuación
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Tiempo sobrevivido: " + score + "s", 10, 30);
            
            // Mostrar nivel de dificultad
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, 18));
            g.drawString("Nivel: " + difficultyLevel, 10, 60);
            
            // Mostrar tiempo hasta próximo nivel
            long timeUntilNextLevel = 30 - ((currentTime - lastDifficultyIncrease) / 1000);
            g.setColor(Color.LIGHT_GRAY);
            g.setFont(new Font("Arial", Font.PLAIN, 14));
            g.drawString("Próximo nivel en: " + timeUntilNextLevel + "s", 10, 85);
            
            // Mostrar indicador de meteoritos si están activos
            if (difficultyLevel >= 6) {
                g.setColor(Color.RED);
                g.setFont(new Font("Arial", Font.BOLD, 16));
                g.drawString("⚠ LLUVIA DE METEORITOS ACTIVA ⚠", 10, 110);
                
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.PLAIN, 12));
                g.drawString("Meteoritos activos: " + meteorites.size(), 10, 125);
            }
            
            // Mostrar controles
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 14));
            String controlText = difficultyLevel >= 6 ? 
                "Flechas: mover nave | ESPACIO: disparar - ¡Esquiva y destruye obstáculos y meteoritos!" : 
                "Flechas: mover nave | ESPACIO: disparar - ¡Esquiva o destruye obstáculos!";
            g.drawString(controlText, 10, PANEL_HEIGHT - 40);
            
            // Mostrar estadísticas de disparos
            g.setColor(Color.CYAN);
            g.setFont(new Font("Arial", Font.PLAIN, 12));
            g.drawString("Balas activas: " + bullets.size(), 10, PANEL_HEIGHT - 20);
            
            // Mostrar mensaje de nivel up si corresponde
            if (showLevelUpMessage) {
                g.setColor(Color.RED);
                g.setFont(new Font("Arial", Font.BOLD, 36));
                FontMetrics fm = g.getFontMetrics();
                String levelUpText = "¡NIVEL " + difficultyLevel + "!";
                g.drawString(levelUpText, (PANEL_WIDTH - fm.stringWidth(levelUpText)) / 2, PANEL_HEIGHT / 2);
                
                g.setColor(Color.ORANGE);
                g.setFont(new Font("Arial", Font.BOLD, 18));
                fm = g.getFontMetrics();
                String difficultyText;
                if (difficultyLevel == 6) {
                    difficultyText = "¡LLUVIA DE METEORITOS ACTIVADA!";
                } else if (difficultyLevel >= 6) {
                    difficultyText = "¡Meteoritos más intensos!";
                } else {
                    difficultyText = "¡Dificultad aumentada!";
                }
                g.drawString(difficultyText, (PANEL_WIDTH - fm.stringWidth(difficultyText)) / 2, PANEL_HEIGHT / 2 + 40);
            }
            
        }
    }
    
    private void drawIntroScreen(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        // Dibujar fondo espacial con estrellas
        drawSpaceBackground(g2d);
        
        // Título principal con efectos 3D
        g2d.setFont(new Font("Arial", Font.BOLD, 64));
        FontMetrics fm = g2d.getFontMetrics();
        String title = "MISIÓN ESTELAR";
        String subtitle = "DE FELIPE";
        
        // Sombra del título en tonos oscuros
        g2d.setColor(new Color(50, 0, 30, 150));
        g2d.drawString(title, (PANEL_WIDTH - fm.stringWidth(title)) / 2 + 3, 120 + 3);
        g2d.drawString(subtitle, (PANEL_WIDTH - fm.stringWidth(subtitle)) / 2 + 3, 190 + 3);
        
        // Título principal con gradiente rojo-rosa
        GradientPaint titleGradient = new GradientPaint(
            PANEL_WIDTH / 2, 100, new Color(255, 50, 100),  // Rosa intenso
            PANEL_WIDTH / 2, 140, new Color(200, 0, 50)     // Rojo profundo
        );
        g2d.setPaint(titleGradient);
        g2d.drawString(title, (PANEL_WIDTH - fm.stringWidth(title)) / 2, 120);
        
        // Subtítulo con gradiente azul-rosa
        GradientPaint subtitleGradient = new GradientPaint(
            PANEL_WIDTH / 2, 170, new Color(100, 150, 255), // Azul claro
            PANEL_WIDTH / 2, 210, new Color(180, 50, 200)   // Rosa-púrpura
        );
        g2d.setPaint(subtitleGradient);
        g2d.drawString(subtitle, (PANEL_WIDTH - fm.stringWidth(subtitle)) / 2, 190);
        
        // Dibujar imagen de Felipe (astronauta) centrada si está cargada
        if (felipeImage != null) {
            int felipeWidth = 180;  // Tamaño más pequeño para no tapar el texto
            int felipeHeight = (int) (felipeImage.getHeight() * ((double) felipeWidth / felipeImage.getWidth()));
            int felipeX = (PANEL_WIDTH - felipeWidth) / 2;  // Centrar horizontalmente
            int felipeY = 280;  // Posición fija para dejar espacio al texto de abajo
            
            // Sombra de Felipe
            g2d.setColor(new Color(0, 0, 0, 100));
            g2d.fillOval(felipeX + 5, felipeY + felipeHeight - 20, felipeWidth, 30);
            
            // Dibujar Felipe con halo
            g2d.setColor(new Color(255, 255, 255, 30));
            g2d.fillOval(felipeX - 10, felipeY - 10, felipeWidth + 20, felipeHeight + 20);
            
            // Configurar composición para transparencia
            g2d.setComposite(AlphaComposite.SrcOver);
            g2d.drawImage(felipeImage, felipeX, felipeY, felipeWidth, felipeHeight, this);
        }
        
        // Instrucciones con animación
        long time = System.currentTimeMillis();
        float alpha = (float) (0.7 + 0.3 * Math.sin(time * 0.003));
        g2d.setColor(new Color(255, 255, 255, (int) (alpha * 255)));
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        fm = g2d.getFontMetrics();
        String startText = "Presiona ENTER para comenzar la misión";
        g2d.drawString(startText, (PANEL_WIDTH - fm.stringWidth(startText)) / 2, PANEL_HEIGHT - 80);
        
        // Controles
        g2d.setColor(new Color(200, 200, 200));
        g2d.setFont(new Font("Arial", Font.PLAIN, 16));
        fm = g2d.getFontMetrics();
        String controlsText = "Controles: Flechas = Mover | ESPACIO = Disparar";
        g2d.drawString(controlsText, (PANEL_WIDTH - fm.stringWidth(controlsText)) / 2, PANEL_HEIGHT - 50);
        
        g2d.dispose();
    }
    
    private void drawGameOverScreen(Graphics g) {
        // Dibujar fondo espacial con estrellas
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawSpaceBackground(g2d);
        
        // Dibujar estrellas de fondo
        for (Star star : stars) {
            star.draw(g2d);
        }
        g2d.dispose();
        
        // Pantalla de game over
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        FontMetrics fm = g.getFontMetrics();
        String gameOverText = "GAME OVER";
        g.drawString(gameOverText, (PANEL_WIDTH - fm.stringWidth(gameOverText)) / 2, PANEL_HEIGHT / 2 - 50);
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        fm = g.getFontMetrics();
        String scoreText = "Tiempo sobrevivido: " + score + " segundos";
        g.drawString(scoreText, (PANEL_WIDTH - fm.stringWidth(scoreText)) / 2, PANEL_HEIGHT / 2);
        
        String restartText = "Presiona ENTER para jugar de nuevo";
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        fm = g.getFontMetrics();
        g.drawString(restartText, (PANEL_WIDTH - fm.stringWidth(restartText)) / 2, PANEL_HEIGHT / 2 + 50);
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        switch (currentState) {
            case INTRO_SCREEN:
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    startGame();
                }
                break;
            case PLAYING:
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        upPressed = true;
                        break;
                    case KeyEvent.VK_DOWN:
                        downPressed = true;
                        break;
                    case KeyEvent.VK_LEFT:
                        leftPressed = true;
                        break;
                    case KeyEvent.VK_RIGHT:
                        rightPressed = true;
                        break;
                    case KeyEvent.VK_SPACE:
                        spacePressed = true;
                        break;
                }
                break;
            case GAME_OVER:
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    currentState = GameState.INTRO_SCREEN;
                    // Volver a reproducir música de introducción
                    if (musicPlayer != null) {
                        musicPlayer.stopMusic();
                        musicPlayer.playIntroMusic();
                    }
                }
                break;
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        if (currentState == GameState.PLAYING) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    upPressed = false;
                    break;
                case KeyEvent.VK_DOWN:
                    downPressed = false;
                    break;
                case KeyEvent.VK_LEFT:
                    leftPressed = false;
                    break;
                case KeyEvent.VK_RIGHT:
                    rightPressed = false;
                    break;
                case KeyEvent.VK_SPACE:
                    spacePressed = false;
                    break;
            }
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}
    
    // Método para limpiar recursos
    public void cleanup() {
        if (musicPlayer != null) {
            musicPlayer.cleanup();
        }
        if (gameTimer != null) {
            gameTimer.stop();
        }
    }
    
    private void drawSpaceBackground(Graphics2D g2d) {
        // Crear gradiente espacial profundo
        GradientPaint spaceGradient = new GradientPaint(
            0, 0, new Color(5, 5, 25),           // Azul muy oscuro arriba
            0, PANEL_HEIGHT, new Color(15, 5, 35) // Púrpura muy oscuro abajo
        );
        g2d.setPaint(spaceGradient);
        g2d.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
        
        // Agregar nebulosas distantes con transparencia
        drawNebula(g2d, PANEL_WIDTH/4, PANEL_HEIGHT/3, 150, new Color(30, 10, 60, 40));
        drawNebula(g2d, 3*PANEL_WIDTH/4, 2*PANEL_HEIGHT/3, 200, new Color(60, 20, 80, 30));
        drawNebula(g2d, PANEL_WIDTH/6, 4*PANEL_HEIGHT/5, 100, new Color(40, 60, 100, 35));
    }
    
    private void drawNebula(Graphics2D g2d, int centerX, int centerY, int size, Color nebulaColor) {
        // Nebulosa con gradiente radial para efecto volumétrico
        RadialGradientPaint nebulaGradient = new RadialGradientPaint(
            centerX, centerY, size,
            new float[]{0.0f, 0.6f, 1.0f},
            new Color[]{
                nebulaColor,
                new Color(nebulaColor.getRed(), nebulaColor.getGreen(), nebulaColor.getBlue(), nebulaColor.getAlpha()/2),
                new Color(nebulaColor.getRed(), nebulaColor.getGreen(), nebulaColor.getBlue(), 0)
            }
        );
        g2d.setPaint(nebulaGradient);
        g2d.fillOval(centerX - size/2, centerY - size/2, size, size);
    }
    
    // Clase interna para las estrellas del fondo con efectos 3D
    private class Star {
        private float x, y;
        private float speed;
        private int size;
        private float brightness;
        private float depth; // Simula la distancia (0.0 = lejos, 1.0 = cerca)
        private boolean isTwinkling;
        private float twinklePhase;
        
        public Star(int x, int y) {
            this.x = x;
            this.y = y;
            this.depth = random.nextFloat(); // Profundidad aleatoria
            this.speed = 0.5f + depth * 2.5f; // Estrellas más cercanas se mueven más rápido (paralaje)
            this.size = (int)(1 + depth * 4); // Estrellas más cercanas son más grandes
            this.brightness = 0.3f + depth * 0.7f; // Estrellas más cercanas son más brillantes
            this.isTwinkling = random.nextFloat() < 0.3f; // 30% de las estrellas titilan
            this.twinklePhase = random.nextFloat() * 6.28f; // Fase inicial aleatoria
        }
        
        public void update() {
            x -= speed;
            if (isTwinkling) {
                twinklePhase += 0.15f;
                if (twinklePhase > 6.28f) twinklePhase -= 6.28f;
            }
        }
        
        public void draw(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Calcular brillo actual (con titileo si aplica)
            float currentBrightness = brightness;
            if (isTwinkling) {
                currentBrightness *= (0.5f + 0.5f * (float)Math.sin(twinklePhase));
            }
            
            // Color basado en profundidad y brillo
            int alpha = (int)(currentBrightness * 255);
            int colorIntensity = (int)(150 + depth * 105); // Estrellas cercanas más blancas
            
            // Estrellas distantes más azuladas, cercanas más blancas/amarillentas
            Color starColor;
            if (depth > 0.7f) {
                // Estrellas cercanas - blanco/amarillento
                starColor = new Color(255, 255, (int)(200 + depth * 55), alpha);
            } else if (depth > 0.3f) {
                // Estrellas medias - blanco
                starColor = new Color(colorIntensity, colorIntensity, 255, alpha);
            } else {
                // Estrellas lejanas - azuladas
                starColor = new Color((int)(colorIntensity * 0.8f), (int)(colorIntensity * 0.9f), 255, alpha);
            }
            
            if (size <= 1) {
                // Estrellas pequeñas - punto simple
                g2d.setColor(starColor);
                g2d.fillRect((int)x, (int)y, 1, 1);
            } else if (size <= 2) {
                // Estrellas medianas - círculo pequeño
                g2d.setColor(starColor);
                g2d.fillOval((int)x, (int)y, size, size);
            } else {
                // Estrellas grandes - con halo y destellos
                drawLargeStar3D(g2d, starColor, currentBrightness);
            }
            
            g2d.dispose();
        }
        
        private void drawLargeStar3D(Graphics2D g2d, Color starColor, float currentBrightness) {
            // Halo exterior
            int haloSize = size + 4;
            Color haloColor = new Color(starColor.getRed(), starColor.getGreen(), starColor.getBlue(), (int)(30 * currentBrightness));
            g2d.setColor(haloColor);
            g2d.fillOval((int)x - 2, (int)y - 2, haloSize, haloSize);
            
            // Núcleo principal
            g2d.setColor(starColor);
            g2d.fillOval((int)x, (int)y, size, size);
            
            // Núcleo brillante
            Color coreColor = new Color(255, 255, 255, (int)(200 * currentBrightness));
            g2d.setColor(coreColor);
            g2d.fillOval((int)x + 1, (int)y + 1, size - 2, size - 2);
            
            // Destellos cruzados (solo para estrellas muy brillantes)
            if (currentBrightness > 0.8f) {
                g2d.setColor(new Color(255, 255, 255, (int)(100 * currentBrightness)));
                g2d.setStroke(new BasicStroke(1));
                int centerX = (int)x + size/2;
                int centerY = (int)y + size/2;
                int sparkleLength = size + 2;
                
                // Destello horizontal
                g2d.drawLine(centerX - sparkleLength/2, centerY, centerX + sparkleLength/2, centerY);
                // Destello vertical  
                g2d.drawLine(centerX, centerY - sparkleLength/2, centerX, centerY + sparkleLength/2);
            }
        }
        
        public void reset(int newX) {
            this.x = newX + random.nextInt(100);
            this.y = random.nextInt(PANEL_HEIGHT);
            // Regenerar propiedades para variedad
            this.depth = random.nextFloat();
            this.speed = 0.5f + depth * 2.5f;
            this.size = (int)(1 + depth * 4);
            this.brightness = 0.3f + depth * 0.7f;
            this.isTwinkling = random.nextFloat() < 0.3f;
            this.twinklePhase = random.nextFloat() * 6.28f;
        }
        
        public int getX() { return (int)x; }
    }
} 