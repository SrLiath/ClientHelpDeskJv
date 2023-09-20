@echo off
for /f "delims=" %%i in ('"C:\Program Files (x86)\AnyDesk\AnyDesk.exe" --get-id') do set ID=%%i 
echo  / AnyDesk: %ID%
