#!/bin/bash

# War Era Launcher for Linux/Ubuntu

# Find Java
JAVA=""
if command -v java &> /dev/null; then
    JAVA=$(which java)
elif [ -f "/usr/bin/java" ]; then
    JAVA="/usr/bin/java"
fi

if [ -z "$JAVA" ]; then
    echo "================================================"
    echo " ERROR: Java no encontrado"
    echo "================================================"
    echo ""
    echo "Para usar War Era Launcher necesitas tener instalado Java."
    echo "En Ubuntu puedes instalar Java con:"
    echo "  sudo apt update"
    echo "  sudo apt install default-jdk"
    echo ""
    echo "================================================"
    read -p "Presiona Enter para salir..."
    exit 1
fi

# Get the directory where the script is located
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Run the JAR
cd "$SCRIPT_DIR"
"$JAVA" -jar "WarEraExplorer.jar"