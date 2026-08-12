#!/bin/bash
echo "==================================================="
echo "  Building and Running Number Quest (Java Swing)  "
echo "==================================================="

mkdir -p bin

echo "Compiling Java source files..."
javac -d bin src/com/numberquest/Main.java src/com/numberquest/model/*.java src/com/numberquest/service/*.java src/com/numberquest/ui/*.java src/com/numberquest/ui/theme/*.java src/com/numberquest/ui/components/*.java

if [ $? -ne 0 ]; then
    echo "[ERROR] Compilation failed!"
    exit 1
fi

echo "[SUCCESS] Compilation successful."
echo "Launching Number Quest..."
echo "==================================================="
java -cp bin com.numberquest.Main
