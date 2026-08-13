@echo off
echo =============================================
echo   ONLINE EXAMINATION SYSTEM
echo =============================================
echo.
echo Compiling project...
javac -d bin src\model\*.java src\util\*.java src\service\*.java src\ui\components\*.java src\ui\*.java src\Main.java

if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Compilation failed. See errors above.
    pause
    exit /b %errorlevel%
)

echo Compilation successful!
echo.
echo Starting application...
java -cp bin Main
pause
