#!/bin/bash

# Script de desarrollo para Juego de Nave Espacial
# Compila y ejecuta el juego automáticamente

echo "🛠️  Modo Desarrollo - Juego de Nave Espacial"
echo "=========================================="

# Compilar el proyecto
echo "🔨 Compilando..."
./scripts/compile.sh

# Verificar si la compilación fue exitosa
if [ $? -eq 0 ]; then
    echo ""
    echo "🎮 Ejecutando juego..."
    echo "=========================================="
    ./scripts/run.sh
else
    echo "❌ La compilación falló. No se puede ejecutar el juego."
    exit 1
fi 