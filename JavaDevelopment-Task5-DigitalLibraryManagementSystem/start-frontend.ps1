# ============================================================
# SMARTLIB AI - Start Frontend Script
# Usage: .\start-frontend.ps1
# ============================================================

Write-Host ""
Write-Host "=========================================" -ForegroundColor Cyan
Write-Host "  SMARTLIB AI - Starting Frontend" -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Frontend will start at: http://localhost:5173" -ForegroundColor Yellow
Write-Host "Make sure backend is running at: http://localhost:8080" -ForegroundColor Gray
Write-Host ""
Write-Host "Press Ctrl+C to stop." -ForegroundColor Gray
Write-Host ""

Set-Location frontend
npm run dev
