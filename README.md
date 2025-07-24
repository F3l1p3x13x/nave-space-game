# 🚀 Misión Estelar de Felipe
### Juego de Nave Espacial Multiplataforma - Java Desktop + Android

![Java](https://img.shields.io/badge/Java-11+-orange?style=for-the-badge&logo=java&logoColor=white)
![Android](https://img.shields.io/badge/Android-21+-green?style=for-the-badge&logo=android&logoColor=white)
![Estado](https://img.shields.io/badge/Estado-Completo-green?style=for-the-badge)
![Plataformas](https://img.shields.io/badge/Plataformas-Desktop%20%7C%20Mobile-blue?style=for-the-badge)

---

## 📋 **Índice**
- [🎮 Descripción del Juego](#-descripción-del-juego)
- [📱 Versiones Disponibles](#-versiones-disponibles)
- [✨ Características Principales](#-características-principales)
- [🛡️ Sistema de Vidas](#-sistema-de-vidas)
- [🎯 Sistema de Detección de Colisiones](#-sistema-de-detección-de-colisiones)
- [🎵 Sistema de Audio](#-sistema-de-audio)
- [🚀 Instalación y Ejecución](#-instalación-y-ejecución)
- [🎮 Controles](#-controles)
- [📊 Sistema de Dificultad](#-sistema-de-dificultad)
- [🏗️ Arquitectura del Código](#-arquitectura-del-código)
- [📁 Estructura del Proyecto](#-estructura-del-proyecto)
- [⚙️ Características Técnicas](#-características-técnicas)
- [🔧 Requisitos del Sistema](#-requisitos-del-sistema)

---

## 🎮 **Descripción del Juego**

**Misión Estelar de Felipe** es un juego de nave espacial multiplataforma desarrollado en Java que combina mecánicas clásicas de supervivencia con tecnologías modernas de renderizado. El jugador controla una nave espacial que debe sobrevivir en el espacio mientras esquiva obstáculos, destruye amenazas y progresa a través de niveles de dificultad creciente.

## 📱 **Versiones Disponibles**

### 🖥️ **Versión Desktop (Branch: `java`)**
- **Plataforma:** Java Swing (Windows, macOS, Linux)
- **Controles:** Teclado (flechas + espacio)
- **Audio:** Sistema MIDI procedural completo
- **Gráficos:** Renderizado 3D avanzado con Java2D

### 📱 **Versión Android (Branch: `android`)**
- **Plataforma:** Android 21+ (API Level 21)
- **Controles:** Touch táctil optimizado para móviles
- **Audio:** Sistema de audio Android nativo
- **Gráficos:** Canvas optimizado para dispositivos móviles
- **Características:** Vibración háptica, pantalla completa

### 🔄 **Cambio entre Versiones**
```bash
# Para la versión Desktop (Java Swing)
git checkout java

# Para la versión Android
git checkout android

# Volver a la rama principal
git checkout main
```

### 🌟 **Historia**
Felipe, un intrépido astronauta, se embarca en una misión espacial peligrosa donde debe navegar a través de campos de asteroides, planetas hostiles y lluvias de meteoritos ardientes para cumplir su misión estelar.

---

## ✨ **Características Principales**

### 🎨 **Gráficos y Visual**
- **Renderizado 3D avanzado** con efectos de gradientes radiales
- **Sistema de partículas** para meteoritos con estelas de fuego
- **Fondo espacial dinámico** con 100+ estrellas animadas con efecto parallax
- **Efectos de transparencia** procesados píxel por píxel
- **Sprites personalizados** con soporte para imágenes PNG externas
- **Interfaz visual mejorada** con corazones animados y indicadores

### 🎯 **Mecánicas de Juego**
- **Sistema de vidas múltiples** (3 vidas iniciales)
- **Reinicio de nivel** tras perder vida (mantiene progreso)
- **Sistema de disparos láser** con efectos 3D
- **Destrucción de obstáculos** con sistema de puntuación bonus
- **Progresión de niveles** cada 20 segundos
- **Activación automática de meteoritos** desde el nivel 6

### 🚀 **Tipos de Enemigos**
- **Asteroides rocosos** con texturas rugosas y cráteres 3D
- **Planetas coloridos** con atmósferas, anillos y características geográficas
- **Meteoritos ardientes** con estelas de fuego y partículas dinámicas

---

## 🛡️ **Sistema de Vidas**

### **Mecánica de Vidas**
- **Inicio:** 3 vidas completas (♥ ♥ ♥)
- **Pérdida de vida:** Al colisionar con obstáculos o meteoritos
- **Reinicio automático:** Vuelve al inicio del nivel actual después de 2 segundos
- **Progreso conservado:** Mantiene el nivel de dificultad alcanzado
- **Game Over:** Solo al agotar las 3 vidas

### **Estados del Juego**
1. **INTRO_SCREEN** - Pantalla de bienvenida con Felipe
2. **PLAYING** - Juego activo
3. **LIFE_LOST** - Pantalla de vida perdida (temporal)
4. **GAME_OVER** - Fin del juego (reinicio completo)

### **Controles de Vidas**
- **ENTER** durante pérdida de vida: Continuar inmediatamente
- **ENTER** en game over: Volver al menú principal

---

## 🎯 **Sistema de Detección de Colisiones**

### **Tecnología Avanzada**
- **Detección multi-zona** en lugar de área rectangular completa
- **Áreas de colisión reducidas** para mayor precisión y justicia
- **Sistema de debugging visual** disponible para desarrolladores

### **Configuración de Precisión**
```java
// Nave espacial
- Área general: 60% del sprite original
- Zona frontal: 30% (punta de la nave)
- Zona central: 40% (cuerpo principal)  
- Zona trasera: 30% (propulsores)

// Obstáculos
- Asteroides: 75% del área original
- Planetas: 80% del área original
- Meteoritos: 70% del área original
```

### **Ventajas del Sistema**
- ✅ Elimina colisiones "fantasma"
- ✅ Gameplay más justo y preciso
- ✅ Mejor experiencia de usuario
- ✅ Detección pixel-perfect opcional

---

## 🎵 **Sistema de Audio**

### **Música Dinámica MIDI**
- **Música épica de introducción** - Melodías cinematográficas con armonías complejas
- **Música espacial del juego** - Secuencias cósmicas procedurales
- **Efectos cósmicos** - Ambientación sci-fi con instrumentos espaciales
- **Transiciones automáticas** entre estados del juego

### **Características del Audio**
```java
// Instrumentos utilizados
- Pad 4 (choir): Sonido espacial principal
- FX 8 (sci-fi): Efectos cósmicos de ambiente  
- Armonías: Múltiples canales con gradientes tonales
```

### **Control de Audio**
- **Reproducción automática** según el estado del juego
- **Loop continuo** para inmersión completa  
- **Gestión de recursos** con limpieza automática
- **Control de volumen** dinámico por canal

---

## 🚀 **Instalación y Ejecución**

### **🖥️ Ejecución Desktop (Branch: java)**
```bash
# Cambiar a la rama Java
git checkout java

# Modo desarrollo (compila y ejecuta automáticamente)
./scripts/dev.sh

# Solo ejecutar (si ya está compilado)
./scripts/run.sh

# Solo compilar
./scripts/compile.sh
```

### **📱 Ejecución Android (Branch: android)**
```bash
# Cambiar a la rama Android
git checkout android

# Compilar APK (requiere Android SDK)
./gradlew assembleDebug

# Instalar en dispositivo conectado
./gradlew installDebug

# Abrir en Android Studio
# File → Open → Seleccionar la carpeta del proyecto
```

### **Ejecución Manual**
```bash
# Compilar
javac -d build/classes -cp src/main/java src/main/java/com/juegofeli/game/*.java

# Copiar recursos
cp -r src/main/resources/* build/classes/

# Ejecutar
cd build/classes && java com.juegofeli.game.SpaceShipGame
```

### **Requisitos de Permisos**
```bash
# Dar permisos de ejecución a los scripts (primera vez)
chmod +x scripts/*.sh
```

---

## 🎮 **Controles**

### **Controles Principales**
| Tecla | Acción | Descripción |
|-------|--------|-------------|
| `↑ ↓ ← →` | **Mover nave** | Control direccional fluido |
| `ESPACIO` | **Disparar** | Proyectiles láser con efectos 3D |
| `ENTER` | **Confirmar** | Iniciar juego / Continuar tras perder vida |

### **Controles por Estado**

#### 🖥️ **Versión Desktop (Java Swing)**
```java
// Pantalla de inicio
ENTER → Comenzar misión

// Durante el juego  
Flechas → Movimiento omnidireccional
ESPACIO → Disparos láser (cooldown: 133ms)

// Vida perdida
ENTER → Reiniciar nivel inmediatamente
Auto → Continuar después de 2 segundos

// Game Over
ENTER → Volver al menú principal
```

#### 📱 **Versión Android (Touch)**
```java
// Pantalla de inicio
Toca pantalla → Comenzar misión

// Durante el juego
Toca y arrastra → Mover nave hacia el dedo
Mantén presionado → Disparar automáticamente (después de 200ms)
Botón pausa → Pausar/reanudar juego

// Vida perdida
Toca pantalla → Continuar inmediatamente
Auto → Continuar después de 2 segundos

// Game Over
Botón atrás → Volver al menú principal
```

---

## 📊 **Sistema de Dificultad**

### **Progresión de Niveles**
- **Duración por nivel:** 20 segundos
- **Incremento automático:** Cada nivel aumenta la dificultad
- **Progresión dinámica:** Sin límite superior de niveles

### **Escalado por Nivel**
```java
// Nivel 1-5: Obstáculos básicos
- Spawn rate: 80 frames → 25 frames (progresivo)
- Velocidad base: 2-4 → aumenta +0.5 cada 2 niveles
- Tamaño: base → +2px por nivel

// Nivel 6+: Lluvia de meteoritos
- Meteoritos activados automáticamente  
- Frecuencia: cada 2 segundos → 0.7 segundos
- Efectos visuales: partículas ardientes
- Advertencia en pantalla: "⚠ LLUVIA DE METEORITOS ACTIVA ⚠"
```

### **Puntuación y Bonus**
```java
// Sistema de scoring
Supervivencia: +1 punto por segundo
Destruir asteroide: +10 puntos bonus  
Destruir meteorito: +20 puntos bonus
Nivel completado: Score base del nivel guardado
```

---

## 🏗️ **Arquitectura del Código**

### **Patrón de Diseño**
```java
// Arquitectura MVC (Model-View-Controller)
Model: SpaceShip, Obstacle, Meteorite, Bullet
View: GamePanel (rendering + UI)  
Controller: GamePanel (input + game logic)
```

### **Clases Principales**

#### **🎮 SpaceShipGame.java**
```java
// Punto de entrada principal
- Configuración de ventana (800x600)
- Gestión del ciclo de vida de la aplicación
- Limpieza automática de recursos
```

#### **🕹️ GamePanel.java** *(1,083 líneas)*
```java
// Motor principal del juego
- Estados: INTRO_SCREEN, PLAYING, LIFE_LOST, GAME_OVER
- Game loop: 60 FPS (~16ms por frame)
- Renderizado: Graphics2D con antialiasing
- Sistema de vidas y reinicio de niveles
- Gestión de entrada de usuario
```

#### **🚀 SpaceShip.java** *(424 líneas)*
```java
// Clase de la nave del jugador
- Carga de sprites PNG con transparencia
- Procesamiento píxel por píxel de imágenes
- Detección de colisiones multi-zona
- Gráficos 3D programáticos como fallback
- Sistema de debugging visual
```

#### **🪨 Obstacle.java** *(221 líneas)*
```java
// Obstáculos espaciales
- Tipos: ASTEROID, PLANET
- Gráficos 3D: gradientes radiales, texturas
- Escalado dinámico según dificultad
- Características únicas por tipo
```

#### **☄️ Meteorite.java** *(192 líneas)*
```java
// Meteoritos con efectos
- Sistema de partículas ardientes
- Movimiento diagonal realista
- Rotación y estelas de fuego 3D
- Expiración automática (3 segundos)
```

#### **💫 Bullet.java** *(86 líneas)*
```java
// Sistema de proyectiles
- Efectos láser 3D con múltiples capas
- Velocidad optimizada (12 px/frame)
- Gestión automática de estado
```

#### **🎵 MusicPlayer.java** *(298 líneas)*
```java
// Sistema de audio MIDI
- Secuencias procedurales dinámicas
- Múltiples pistas e instrumentos
- Gestión automática de estados
- Efectos cósmicos ambientales
```

---

## 📁 **Estructura del Proyecto**

```
JUEGOFELI/
├── src/main/
│   ├── java/com/juegofeli/game/
│   │   ├── SpaceShipGame.java      # Entry point
│   │   ├── GamePanel.java          # Motor principal
│   │   ├── SpaceShip.java          # Nave del jugador
│   │   ├── Obstacle.java           # Asteroides y planetas
│   │   ├── Meteorite.java          # Meteoritos ardientes
│   │   ├── Bullet.java             # Sistema de disparos
│   │   └── MusicPlayer.java        # Audio MIDI
│   └── resources/images/
│       ├── felipe.png              # Astronauta (1024x1024)
│       └── nave_space_ship.png     # Sprite de nave (3.3MB)
├── scripts/
│   ├── compile.sh                  # Script de compilación
│   ├── run.sh                      # Script de ejecución
│   └── dev.sh                      # Desarrollo (compila + ejecuta)
├── build/                          # Archivos compilados
├── docs/                           # Documentación adicional
└── README.md                       # Esta documentación
```

---

## ⚙️ **Características Técnicas**

### **Rendimiento y Optimización**
```java
// Motor de juego
FPS: 60 (16ms por frame)
Resolución: 800x600 píxeles fijos
Objetos activos: Sin límite (gestión automática)
Limpieza automática: Objetos fuera de pantalla

// Gráficos
Renderizado: Java2D con aceleración hardware
Antialiasing: Activado globalmente
Transparencia: Procesamiento alfa optimizado
Efectos: Gradientes radiales, partículas, sombras
```

### **Gestión de Memoria**
```java
// Optimizaciones implementadas
- Reuso de objetos Graphics2D
- Limpieza automática de proyectiles
- Gestión de ciclo de vida de meteoritos
- Liberación de recursos de audio
- Iterator pattern para colecciones
```

### **Sistemas Avanzados**
```java
// Características técnicas destacadas
✅ Detección de colisiones multi-zona
✅ Sistema de partículas para efectos
✅ Música MIDI procedural
✅ Procesamiento de transparencia píxel-perfect
✅ Efecto parallax en estrellas de fondo
✅ Sistema de estados robusto
✅ Gestión automática de recursos
```

---

## 🔧 **Requisitos del Sistema**

### **Requisitos Mínimos**
- **Java:** JDK 11 o superior
- **OS:** Windows, macOS, Linux
- **RAM:** 256 MB disponible
- **Espacio:** 10 MB libres
- **Audio:** Compatibilidad MIDI (opcional)

### **Requisitos Recomendados**
- **Java:** JDK 17+ LTS
- **RAM:** 512 MB+ disponible  
- **GPU:** Aceleración hardware Java2D
- **Audio:** Sistema de sonido integrado
- **Pantalla:** 1024x768+ resolución

### **Dependencias**
```java
// Librerías estándar incluidas
java.awt.*          // Gráficos y UI
javax.swing.*       // Interfaz de usuario
javax.sound.midi.*  // Sistema de audio
java.util.*         // Estructuras de datos
```

---

## 🏆 **Logros del Desarrollo**

### **Mejoras Implementadas**
1. ✅ **Sistema de vidas completo** con reinicio de nivel
2. ✅ **Detección de colisiones precisa** multi-zona  
3. ✅ **Niveles de 20 segundos** para mayor dinamismo
4. ✅ **Música procedural MIDI** con múltiples pistas
5. ✅ **Gráficos 3D avanzados** con efectos de partículas
6. ✅ **Sistema de debugging visual** para desarrolladores
7. ✅ **Arquitectura modular** y extensible
8. ✅ **Gestión automática de recursos** y memoria

### **Estadísticas del Código**
- **Líneas totales:** ~2,400+ líneas
- **Archivos Java:** 7 clases principales
- **Métodos:** 80+ métodos implementados  
- **Estados de juego:** 4 estados manejados
- **Sistemas:** 8 subsistemas integrados

---

## 🎯 **Gameplay y Estrategia**

### **Estrategias de Supervivencia**
1. **Movimiento defensivo:** Mantén distancia de los bordes
2. **Disparo estratégico:** Destruye obstáculos para ganar puntos
3. **Gestión de vidas:** Usa las 3 vidas sabiamente
4. **Nivel 6+:** Prioriza esquivar meteoritos sobre destruirlos

### **Mecánicas Avanzadas**
- **Cooldown de disparo:** 133ms entre proyectiles
- **Velocidad de meteoritos:** Diagonal realista (2-5 px/frame)
- **Spawn rate dinámico:** Aceleración progresiva por nivel
- **Sistema de scoring:** Supervivencia + bonus por destrucción

---

## 👨‍💻 **Para Desarrolladores**

### **Debug Mode**
```java
// Activar visualización de áreas de colisión
// En SpaceShip.java, línea 181:
drawDebugCollisionBounds(g2d); // Descomentar esta línea
```

### **Personalización**
```java
// Configuraciones editables
GamePanel.java:
- PANEL_WIDTH/HEIGHT: Resolución de ventana
- MAX_LIVES: Número de vidas iniciales
- SHOOT_COOLDOWN_TIME: Velocidad de disparo

SpaceShip.java:  
- width/height: Tamaño de la nave
- speed: Velocidad de movimiento
```

### **Extensiones Futuras**
- [ ] Power-ups y mejoras de nave
- [ ] Múltiples tipos de proyectiles
- [ ] Jefes de nivel (boss fights)
- [ ] Sistema de puntuación global
- [ ] Multijugador local
- [ ] Más tipos de obstáculos

---

## 📄 **Licencia**
Este proyecto es de código abierto y está disponible bajo la licencia MIT.

## 🤝 **Contribuciones**
Las contribuciones son bienvenidas. Por favor, abre un issue antes de realizar cambios mayores.

---

**🚀 ¡Feliz vuelo espacial, Felipe! ¡Que tengas una misión estelar exitosa! 🌟** 