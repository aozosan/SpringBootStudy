@echo off
call :run mysocket
call :run myclient
pause
exit /b

:run
echo.
echo %1
cd %1
call gradlew build --warning-mode all
cd ..
exit /b