#!/bin/bash

# Script de compilación para Juego de Nave Espacial
echo "🔨 Compilando Juego de Nave Espacial..."

# Crear directorio de clases si no existe
mkdir -p build/classes

# Compilar todos los archivos Java
javac -d build/classes -cp src/main/java src/main/java/com/juegofeli/game/*.java

# Verificar si la compilación fue exitosa
if [ $? -eq 0 ]; then
    echo "✅ Compilación exitosa!"
    echo "📁 Archivos compilados en: build/classes/"
    
    # Copiar recursos al directorio de clases
    if [ -d "src/main/resources" ]; then
        echo "📋 Copiando recursos..."
        cp -r src/main/resources/* build/classes/
        echo "✅ Recursos copiados exitosamente!"
    fi
    
    echo ""
    echo "🚀 Para ejecutar el juego, usa: ./scripts/run.sh"
else
    echo "❌ Error en la compilación"
    exit 1
fi 