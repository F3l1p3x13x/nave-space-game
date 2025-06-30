#!/bin/bash

# Script de ejecución para Juego de Nave Espacial
echo "🚀 Iniciando Juego de Nave Espacial..."

# Verificar si existe el directorio de clases compiladas
if [ ! -d "build/classes" ]; then
    echo "❌ No se encontraron archivos compilados"
    echo "🔨 Ejecuta primero: ./scripts/compile.sh"
    exit 1
fi

# Verificar si existe la clase principal
if [ ! -f "build/classes/com/juegofeli/game/SpaceShipGame.class" ]; then
    echo "❌ Clase principal no encontrada"
    echo "🔨 Ejecuta primero: ./scripts/compile.sh"
    exit 1
fi

# Ejecutar el juego
echo "🎮 Lanzando juego..."
echo "📋 Controles:"
echo "   - Flechas: Mover nave"
echo "   - ESPACIO: Disparar"
echo "   - ENTER: Reiniciar (después de game over)"
echo ""

cd build/classes && java com.juegofeli.game.SpaceShipGame 