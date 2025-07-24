#!/bin/bash

echo "🚀 Generador de APK para Misión Estelar de Felipe"
echo "================================================="
echo ""

# Check if we're on Android branch
current_branch=$(git branch --show-current)
if [ "$current_branch" != "android" ]; then
    echo "❌ Error: Debes estar en el branch 'android' para generar el APK"
    echo "💡 Ejecuta: git checkout android"
    exit 1
fi

echo "✅ Branch correcto: $current_branch"
echo ""

# Check Java
if ! command -v java &> /dev/null; then
    echo "❌ Error: Java no está instalado"
    echo "💡 Instala Java 11+ desde: https://adoptium.net/"
    exit 1
fi

java_version=$(java -version 2>&1 | head -1 | cut -d'"' -f2)
echo "✅ Java encontrado: $java_version"

# Check Android SDK
if [ -z "$ANDROID_HOME" ] && [ -z "$ANDROID_SDK_ROOT" ]; then
    echo ""
    echo "⚠️  ANDROID_HOME no está configurado"
    echo ""
    echo "📱 OPCIONES PARA GENERAR EL APK:"
    echo ""
    echo "1️⃣  USANDO ANDROID STUDIO (RECOMENDADO):"
    echo "   - Descarga Android Studio: https://developer.android.com/studio"
    echo "   - Abre el proyecto desde: File → Open → Selecciona esta carpeta"
    echo "   - Espera que se sincronize"
    echo "   - Ve a: Build → Generate Signed Bundle/APK"
    echo "   - Selecciona APK → Create new keystore → Sigue el asistente"
    echo "   - ¡APK listo para instalar!"
    echo ""
    echo "2️⃣  USANDO LÍNEA DE COMANDOS (AVANZADO):"
    echo "   - Instala Android SDK: https://developer.android.com/studio#cmdline-tools"
    echo "   - Configura ANDROID_HOME en tu ~/.bashrc o ~/.zshrc:"
    echo "     export ANDROID_HOME=\$HOME/Android/Sdk"
    echo "     export PATH=\$PATH:\$ANDROID_HOME/tools:\$ANDROID_HOME/platform-tools"
    echo "   - Recarga tu terminal: source ~/.bashrc"
    echo "   - Ejecuta este script nuevamente"
    echo ""
    echo "3️⃣  USANDO GRADLE WRAPPER (SI ANDROID_HOME ESTÁ CONFIGURADO):"
    echo "   ./gradlew assembleDebug"
    echo "   # APK generado en: app/build/outputs/apk/debug/"
    echo ""
else
    echo "✅ ANDROID_HOME configurado: ${ANDROID_HOME:-$ANDROID_SDK_ROOT}"
    echo ""
    
    # Try to build with gradle wrapper
    echo "🔨 Intentando generar APK..."
    
    if [ ! -f "gradlew" ]; then
        echo "❌ gradlew no encontrado"
        exit 1
    fi
    
    # Make gradlew executable
    chmod +x gradlew
    
    echo "📦 Descargando dependencias y compilando..."
    ./gradlew assembleDebug
    
    if [ $? -eq 0 ]; then
        echo ""
        echo "🎉 ¡APK GENERADO EXITOSAMENTE!"
        echo ""
        apk_path="app/build/outputs/apk/debug/app-debug.apk"
        if [ -f "$apk_path" ]; then
            echo "📱 APK ubicado en: $apk_path"
            echo "📏 Tamaño: $(du -h $apk_path | cut -f1)"
            echo ""
            echo "🔧 INSTALACIÓN EN DISPOSITIVO:"
            echo "   1. Habilita 'Desarrollador' en tu Android:"
            echo "      Configuración → Acerca del teléfono → Toca 'Número de compilación' 7 veces"
            echo "   2. Habilita 'Depuración USB':"
            echo "      Configuración → Opciones de desarrollador → Depuración USB"
            echo "   3. Conecta tu dispositivo por USB"
            echo "   4. Instala el APK:"
            echo "      adb install $apk_path"
            echo "   O simplemente transfiere el APK al dispositivo y ábrelo"
            echo ""
            echo "🎮 ¡Disfruta jugando Misión Estelar de Felipe en tu Android!"
        fi
    else
        echo ""
        echo "❌ Error durante la compilación"
        echo "💡 Revisa que tengas Android SDK instalado y configurado"
        echo "💡 O usa Android Studio para compilar el proyecto"
    fi
fi

echo ""
echo "📚 Documentación completa en README.md"
echo "🌟 Proyecto en: https://github.com/F3l1p3x13x/nave-space-game" 