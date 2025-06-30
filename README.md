# Juego de Nave Espacial

Un juego de nave espacial en Java donde debes esquivar planetas y asteroides el mayor tiempo posible.

## Características del Juego

- **Nave espacial**: Controlada con las flechas del teclado
- **Obstáculos**: Planetas y asteroides que aparecen aleatoriamente
- **Objetivo**: Sobrevivir el mayor tiempo posible sin chocar
- **Puntuación**: Tiempo sobrevivido en segundos
- **Dificultad progresiva**: Cada 30 segundos aumenta la dificultad con obstáculos más rápidos, grandes y frecuentes
- **Lluvia de meteoritos**: A partir del nivel 6, meteoritos caen en diagonal durante 3 segundos
- **Gráficos 3D**: Efectos visuales pseudo-3D con gradientes, sombras y profundidad
- **Música espacial**: Banda sonora MIDI generada dinámicamente con melodías y efectos ambientales

## Controles

- **Flechas del teclado**: Mover la nave (arriba, abajo, izquierda, derecha)
- **Barra espaciadora**: Reiniciar el juego después de Game Over (reseteo completo a estado inicial)

## Cómo Compilar y Ejecutar

### Requisitos
- Java 8 o superior instalado en tu sistema

### Compilación
```bash
javac *.java
```

### Ejecución
```bash
java SpaceShipGame
```

## Archivos del Proyecto

- `SpaceShipGame.java`: Clase principal que inicia el juego
- `GamePanel.java`: Panel del juego que maneja toda la lógica
- `SpaceShip.java`: Clase que representa la nave espacial del jugador
- `Obstacle.java`: Clase que representa planetas y asteroides
- `Meteorite.java`: Clase que representa meteoritos que caen diagonalmente
- `MusicPlayer.java`: Sistema de reproducción de música MIDI espacial

## Reglas del Juego

1. Usa las flechas del teclado para mover tu nave espacial
2. Evita chocar con planetas (círculos grandes de colores) y asteroides (círculos marrones más pequeños)
3. El juego termina cuando chocas con cualquier obstáculo
4. Tu puntuación es el tiempo que logres sobrevivir (en segundos)
5. **Sistema de niveles**: Cada 30 segundos la dificultad aumenta automáticamente
   - Los obstáculos aparecen más frecuentemente
   - Se mueven más rápido
   - Son ligeramente más grandes
   - **NIVEL 6+**: ¡Lluvia de meteoritos activada!
   - Aparece un mensaje "¡NIVEL X!" cuando subes de nivel
6. **Meteoritos (Nivel 6+)**:
   - Caen diagonalmente desde arriba
   - Duran exactamente 3 segundos antes de desaparecer
   - Aparecen aleatoriamente con mayor frecuencia en niveles altos
   - Tienen estelas de fuego y rotación visual
   - Son más pequeños pero igual de letales que los obstáculos normales
7. La pantalla muestra tu nivel actual, tiempo hasta próximo nivel y meteoritos activos
8. ¡Intenta superar tu récord personal y alcanzar el nivel más alto!

## Características Técnicas

- Desarrollado con Java Swing y Graphics2D
- Frecuencia de actualización: ~60 FPS
- Resolución: 800x600 píxeles
- Detección de colisiones basada en rectángulos delimitadores
- **Motor Gráfico 3D Avanzado**:
  - Anti-aliasing completo para suavizado de bordes
  - Gradientes radiales y lineales para efectos volumétricos
  - Sistema de transparencias y composición alfa
  - Efectos de iluminación y sombras proyectadas
  - Paralaje multicapa para sensación de profundidad
  - Rotaciones y transformaciones en tiempo real
- **Sistema de audio MIDI**:
  - Música espacial generada programáticamente
  - 3 canales de audio: melodía principal, armonía y efectos ambientales
  - Instrumentos sintetizadores espaciales (Pad Choir, Warm Pad, Sci-Fi FX)
  - Reproducción en loop continuo durante el juego
  - Gestión automática de recursos de audio

## 🎨 Efectos Visuales 3D

El juego cuenta con gráficos pseudo-3D avanzados que crean una experiencia visual inmersiva:

### **🚀 Nave Espacial 3D**
- **Gradientes metálicos**: Efectos de iluminación realistas con brillos y sombras
- **Cabina de cristal**: Reflejos y transparencias que simulan vidrio espacial
- **Propulsores ardientes**: Llamas con gradientes radiales y núcleos brillantes
- **Sombras proyectadas**: Efectos de profundidad para mayor realismo

### **🌍 Planetas Realistas**
- **Gradientes esféricos**: Iluminación que simula la curvatura planetaria
- **Características geográficas**: Continentes y océanos con transparencias
- **Anillos planetarios**: Perspectiva elíptica con efectos de profundidad
- **Atmósferas**: Halos sutiles alrededor de los planetas
- **Reflejos especulares**: Brillos que simulan luz solar

### **☄️ Asteroides Texturizados**
- **Superficie rugosa**: Cráteres 3D con gradientes cóncavos
- **Iluminación direccional**: Efectos de luz y sombra realistas
- **Bordes iluminados**: Reflejos que dan sensación de volumen

### **🌠 Meteoritos Ardientes**
- **Estelas de fuego avanzadas**: 8 capas de gradientes con partículas
- **Núcleos ardientes**: Múltiples capas de intensidad lumínica
- **Rotación realista**: Movimiento tridimensional convincente
- **Halos de calor**: Efectos de energía alrededor del meteorito

### **✨ Campo Estelar 3D**
- **Paralaje espacial**: Estrellas a diferentes profundidades se mueven a velocidades distintas
- **Titileo realista**: 30% de las estrellas titilan con fases aleatorias
- **Colores de temperatura**: Estrellas cercanas amarillentas, lejanas azuladas
- **Destellos cruzados**: Estrellas brillantes con efectos de difracción
- **Halos graduales**: Efectos de brillo que simulan atmósfera

### **🌌 Fondo Espacial Profundo**
- **Gradientes atmosféricos**: Transición de azul oscuro a púrpura espacial
- **Nebulosas distantes**: Efectos volumétricos con transparencias
- **Profundidad visual**: Múltiples capas que crean sensación de infinito

## 🌟 Desafío Especial: ¡Nivel 6!

A partir del nivel 6 (después de 2 minutos y 30 segundos), el juego se transforma completamente con la **Lluvia de Meteoritos**. Estos pequeños proyectiles ardientes caen diagonalmente y crean un patrón de esquiva completamente nuevo. ¡La verdadera prueba de habilidad comienza aquí!

## 🐛 Correcciones de Bugs

- **Bug de Reinicio Corregido**: Ahora al reiniciar el juego después de perder, todos los parámetros vuelven exactamente a su estado inicial:
  - Frecuencia de spawn de obstáculos resetea a valor inicial (80 frames)
  - Posición de la nave vuelve al centro-izquierda inicial
  - Estado de teclas presionadas se limpia completamente
  - Nivel de dificultad vuelve a 1
  - Listas de obstáculos y meteoritos se vacían completamente

¡Disfruta el juego y trata de sobrevivir el mayor tiempo posible! 