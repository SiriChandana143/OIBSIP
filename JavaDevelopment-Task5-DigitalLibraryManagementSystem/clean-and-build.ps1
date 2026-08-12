# ============================================================
# SMARTLIB AI - Clean Build Helper Script
# Fixes "Failed to delete target/..." locked directory issue
# caused by IDE Java processes holding file locks.
# ============================================================

Write-Host "Stopping any running Java processes..." -ForegroundColor Yellow
Get-Process | Where-Object { $_.ProcessName -like "*java*" } | ForEach-Object {
    Write-Host "  Stopping PID $($_.Id) - $($_.MainWindowTitle)" -ForegroundColor Gray
    Stop-Process -Id $_.Id -Force -ErrorAction SilentlyContinue
}
Start-Sleep -Seconds 2

Write-Host "Removing target directory..." -ForegroundColor Yellow
Remove-Item -Recurse -Force "backend\target" -ErrorAction SilentlyContinue
Write-Host "Target cleaned." -ForegroundColor Green

# Use JDK 21 (required for Lombok compatibility with this project)
$env:JAVA_HOME = "C:\Users\ASUS\.vscode\extensions\redhat.java-1.55.0-win32-x64\jre\21.0.11-win32-x86_64"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"

Write-Host "Using Java: $(java -version 2>&1 | Select-Object -First 1)" -ForegroundColor Cyan
Write-Host ""
Write-Host "Running mvn clean test..." -ForegroundColor Yellow
Set-Location backend
mvn clean test
