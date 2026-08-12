# ============================================================
# SMARTLIB AI - Start Backend Script
# Usage: .\start-backend.ps1
# Usage: .\start-backend.ps1 -DbPassword "yourpassword"
# ============================================================
param(
    [string]$DbPassword = "root"
)

Write-Host ""
Write-Host "=========================================" -ForegroundColor Cyan
Write-Host "  SMARTLIB AI - Starting Backend" -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan

# Use JDK 21 for Lombok compatibility
$env:JAVA_HOME = "C:\Users\ASUS\.vscode\extensions\redhat.java-1.55.0-win32-x64\jre\21.0.11-win32-x86_64"
$env:Path      = "$env:JAVA_HOME\bin;$env:Path"

# Set database password
$env:DB_PASSWORD = $DbPassword

Write-Host "Java: $(java -version 2>&1 | Select-Object -First 1)" -ForegroundColor Gray
Write-Host "DB Password: $DbPassword" -ForegroundColor Gray
Write-Host ""

Write-Host "Starting Spring Boot on http://localhost:8080 ..." -ForegroundColor Yellow
Write-Host "Swagger UI:  http://localhost:8080/swagger-ui.html" -ForegroundColor Yellow
Write-Host ""
Write-Host "Press Ctrl+C to stop." -ForegroundColor Gray
Write-Host ""

Set-Location backend
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-DDB_PASSWORD=$DbPassword"
