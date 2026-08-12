@echo off
echo ==========================================
echo  SMARTLIB AI - MySQL Password Reset Tool
echo  Run this as ADMINISTRATOR
echo ==========================================
echo.

echo [1/3] Stopping MySQL80 service...
net stop MySQL80
timeout /t 3

echo [2/3] Starting MySQL with skip-grant-tables...
start /B "mysqld_reset" "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysqld.exe" --defaults-file="C:\ProgramData\MySQL\MySQL Server 8.0\my.ini" --skip-grant-tables --skip-networking
timeout /t 5

echo [3/3] Resetting root password to 'root'...
"C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -u root --connect-expired-password -e "FLUSH PRIVILEGES; ALTER USER 'root'@'localhost' IDENTIFIED BY 'root'; FLUSH PRIVILEGES;"

echo Stopping temporary MySQL instance...
taskkill /F /IM mysqld.exe /T 2>nul
timeout /t 3

echo Starting MySQL80 service normally...
net start MySQL80
timeout /t 3

echo.
echo ==========================================
echo  Done! MySQL root password is now: root
echo  Test: mysql -u root -proot -e "SELECT 1;"
echo ==========================================
pause
