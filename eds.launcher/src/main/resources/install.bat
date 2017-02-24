echo "Registered startup key"
REG ADD HKEY_CURRENT_USER\SOFTWARE\Microsoft\Windows\CurrentVersion\Run /f /v EDS /t REG_SZ /d %cd%\eds\eds.exe


echo "Launch"
%cd%\eds\EDS.exe

echo "Install success, press any key exit!"
pause