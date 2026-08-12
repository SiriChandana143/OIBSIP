@echo off
echo ===================================================
echo   Building and Running Number Quest (Java Swing)  
echo ===================================================

if not exist "bin" mkdir bin

echo Compiling Java source files...
javac -d bin src/com/numberquest/Main.java src/com/numberquest/model/*.java src/com/numberquest/service/*.java src/com/numberquest/ui/*.java src/com/numberquest/ui/theme/*.java src/com/numberquest/ui/components/*.java

if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Compilation failed!
    pause
    exit /b %ERRORLEVEL%
)

echo [SUCCESS] Compilation successful.
echo Launching Number Quest...
echo ===================================================
java -cp bin com.numberquest.Main

pause
