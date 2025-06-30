# 🚀 Juego de Nave Espacial

Un emocionante juego de nave espacial desarrollado en Java con gráficos 2D avanzados, sistema de disparos, niveles progresivos y música MIDI espacial.

## 📋 Características

### 🎮 Mecánicas de Juego
- **Nave espacial controlable** con flechas del teclado
- **Sistema de disparos** con láser espacial (barra espaciadora)
- **Obstáculos variados**: Planetas coloridos y asteroides peligrosos
- **Lluvia de meteoritos** desde el nivel 6 con efectos ardientes
- **Sistema de niveles progresivos** cada 30 segundos
- **Puntuación basada** en tiempo sobrevivido + enemigos destruidos

### 🎨 Gráficos Avanzados
- **Gráficos pseudo-3D** con gradientes y efectos volumétricos
- **Campo estelar dinámico** con paralaje y titilación
- **Efectos de partículas** para propulsores y meteoritos
- **Soporte para imagen PNG personalizada** de la nave
- **Fondo espacial** con nebulosas y gradientes atmosféricos

### 🎵 Audio
- **Música MIDI espacial** generada programáticamente
- **3 canales de audio**: melodía, armonía y efectos ambientales
- **Reproducción en loop** continuo durante el juego

## 🏗️ Estructura del Proyecto

```
JUEGOFELI/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── juegofeli/
│   │   │           └── game/
│   │   │               ├── SpaceShipGame.java     # Clase principal
│   │   │               ├── GamePanel.java         # Motor del juego
│   │   │               ├── SpaceShip.java         # Nave espacial
│   │   │               ├── Bullet.java            # Sistema de disparos
│   │   │               ├── Obstacle.java          # Obstáculos (planetas/asteroides)
│   │   │               ├── Meteorite.java         # Meteoritos ardientes
│   │   │               └── MusicPlayer.java       # Reproductor MIDI
│   │   └── resources/
│   │       └── images/
│   │           └── nave_space_ship.png           # Imagen personalizada de la nave
├── build/
│   └── classes/                                  # Archivos compilados
├── docs/                                         # Documentación
├── scripts/
│   ├── compile.sh                               # Script de compilación
│   ├── run.sh                                   # Script de ejecución  
│   └── dev.sh                                   # Desarrollo (compila + ejecuta)
├── .gitignore                                   # Exclusiones de Git
└── README.md                                    # Este archivo
```

## 🚀 Instalación y Ejecución

### Prerrequisitos
- **Java JDK 8+** instalado
- **Git** para clonar el repositorio

### Clonar el Repositorio
```bash
git clone https://github.com/F3l1p3x13x/nave-space-game.git
cd nave-space-game
```

### Compilación y Ejecución

#### Opción 1: Modo Desarrollo (Recomendado)
```bash
./scripts/dev.sh
```
*Compila y ejecuta automáticamente*

#### Opción 2: Manual
```bash
# Compilar
./scripts/compile.sh

# Ejecutar
./scripts/run.sh
```

#### Opción 3: Comando Directo
```bash
# Compilar
javac -d build/classes -cp src/main/java src/main/java/com/juegofeli/game/*.java
cp -r src/main/resources/* build/classes/

# Ejecutar
cd build/classes && java com.juegofeli.game.SpaceShipGame
```

## 🎮 Controles

| Tecla | Acción |
|-------|--------|
| **↑ ↓ ← →** | Mover nave espacial |
| **ESPACIO** | Disparar láser |
| **ENTER** | Reiniciar juego (después de Game Over) |

## 🎯 Sistema de Juego

### Niveles de Dificultad
- **Nivel 1-5**: Obstáculos básicos (planetas y asteroides)
- **Nivel 6+**: Se activa la lluvia de meteoritos ardientes
- **Escalado**: Cada nivel aumenta velocidad y frecuencia de enemigos

### Sistema de Puntuación
- **+1 punto** por segundo sobrevivido
- **+10 puntos** por cada obstáculo destruido
- **+20 puntos** por cada meteorito destruido

### Tipos de Enemigos
1. **Planetas**: Círculos grandes con atmósferas y características geográficas
2. **Asteroides**: Superficies rugosas con cráteres e iluminación realista
3. **Meteoritos**: (Nivel 6+) Proyectiles ardientes con estelas de fuego

## 🛠️ Desarrollo

### Arquitectura
- **Patrón MVC**: Separación clara entre modelo, vista y controlador
- **Estructura de paquetes Java estándar**: `com.juegofeli.game`
- **Gestión de recursos**: Sistema de carga desde `resources/`
- **Renderizado optimizado**: Graphics2D con anti-aliasing

### Características Técnicas
- **Engine**: Java Swing con Timer para loop de juego (~60 FPS)
- **Gráficos**: Java 2D API con efectos avanzados
- **Audio**: Java Sound API para MIDI
- **Colisiones**: Sistema de Rectangle bounds intersection
- **Memoria**: Gestión automática de objetos fuera de pantalla

## 🎨 Personalización

### Cambiar Imagen de la Nave
1. Reemplazar `src/main/resources/images/nave_space_ship.png`
2. Recomendado: PNG con transparencia, tamaño 64x64 píxeles o similar
3. Recompilar el proyecto

### Modificar Parámetros de Juego
Editar constantes en `GamePanel.java`:
- `SHOOT_COOLDOWN_TIME`: Velocidad de disparo
- `obstacleSpawnDelay`: Frecuencia de obstáculos
- Velocidades de movimiento en las clases individuales

## 📊 Estadísticas del Proyecto

- **Archivos Java**: 7 clases principales
- **Líneas de código**: ~1,400 líneas
- **Funcionalidades**: 15+ características implementadas
- **Efectos gráficos**: 20+ tipos diferentes
- **Compatibilidad**: Java 8 - Java 21

## 🤝 Contribuir

1. Fork el proyecto
2. Crear una branch de feature (`git checkout -b feature/NuevaCaracteristica`)
3. Commit los cambios (`git commit -m 'Agregar nueva característica'`)
4. Push a la branch (`git push origin feature/NuevaCaracteristica`)
5. Abrir un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver archivo [LICENSE](LICENSE) para detalles.

## 🏆 Créditos

- **Desarrollo**: Equipo de desarrollo del juego
- **Motor gráfico**: Java 2D API
- **Audio**: Java Sound API (MIDI)
- **Inspiración**: Juegos clásicos de naves espaciales tipo Asteroids

---

**¡Disfruta el juego y que tengas vuelos espaciales épicos! 🌌** 