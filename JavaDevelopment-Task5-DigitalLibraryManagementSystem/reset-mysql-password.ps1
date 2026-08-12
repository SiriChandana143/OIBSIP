# ============================================================
# SMARTLIB AI - MySQL Password Reset Script (PowerShell)
# MUST run as Administrator (right-click > Run as Administrator)
# ============================================================

Write-Host ""
Write-Host "=========================================" -ForegroundColor Cyan
Write-Host " SMARTLIB AI - MySQL Password Reset" -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan
Write-Host ""

$mysqldPath = "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysqld.exe"
$mysqlPath  = "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
$myIni      = "C:\ProgramData\MySQL\MySQL Server 8.0\my.ini"

# Step 1: Stop the normal MySQL service
Write-Host "[1/5] Stopping MySQL80 service..." -ForegroundColor Yellow
net stop MySQL80 2>&1 | Out-Null
Start-Sleep -Seconds 3

# Step 2: Kill any leftover mysqld processes
Write-Host "[2/5] Killing any remaining mysqld processes..." -ForegroundColor Yellow
Get-Process -Name "mysqld" -ErrorAction SilentlyContinue | Stop-Process -Force
Start-Sleep -Seconds 2

# Step 3: Start mysqld with --skip-grant-tables
Write-Host "[3/5] Starting MySQL with --skip-grant-tables..." -ForegroundColor Yellow
$proc = Start-Process -FilePath $mysqldPath `
    -ArgumentList "--defaults-file=`"$myIni`" --skip-grant-tables --skip-networking" `
    -PassThru -WindowStyle Hidden
Start-Sleep -Seconds 6
Write-Host "      MySQL started (PID: $($proc.Id))" -ForegroundColor Gray

# Step 4: Reset the password
Write-Host "[4/5] Resetting root password to 'root'..." -ForegroundColor Yellow
$sql = "FLUSH PRIVILEGES; ALTER USER 'root'@'localhost' IDENTIFIED BY 'root'; FLUSH PRIVILEGES;"
& $mysqlPath -u root --connect-expired-password -e $sql 2>&1
if ($LASTEXITCODE -eq 0) {
    Write-Host "      Password reset successful!" -ForegroundColor Green
} else {
    Write-Host "      Password reset may have failed. Check above output." -ForegroundColor Red
}

# Step 5: Stop temp mysqld and restart service normally
Write-Host "[5/5] Restarting MySQL80 service normally..." -ForegroundColor Yellow
Stop-Process -Name "mysqld" -Force -ErrorAction SilentlyContinue
Start-Sleep -Seconds 3
net start MySQL80 2>&1 | Out-Null
Start-Sleep -Seconds 4

# Verify
Write-Host ""
Write-Host "Verifying connection with root/root..." -ForegroundColor Yellow
$verify = & $mysqlPath -u root -proot -e "SELECT 'Connection OK' AS Status;" 2>&1
if ($verify -match "Connection OK") {
    Write-Host "SUCCESS! MySQL is accessible with root/root" -ForegroundColor Green
} else {
    Write-Host "Verification output:" -ForegroundColor Red
    Write-Host $verify
}

Write-Host ""
Write-Host "=========================================" -ForegroundColor Cyan
Write-Host " Done! Password is now: root" -ForegroundColor Green
Write-Host " You can now start the SMARTLIB backend" -ForegroundColor Green
Write-Host "=========================================" -ForegroundColor Cyan
