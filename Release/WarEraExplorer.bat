@echo off
setlocal

REM Find any Java installation
set "JAVA="

REM Check common Java installation paths
set "JAVA_PATHS=C:\Program Files\Java\bin\java.exe;C:\Program Files (x86)\Java\bin\java.exe;C:\Program Files\Common Files\Oracle\Java\javapath\java.exe;C:\Program Files\Java\jdk-23\bin\java.exe;C:\Program Files\Java\jdk-17\bin\java.exe;C:\Program Files\Java\jdk-11\bin\java.exe;C:\Program Files\Java\jdk-8\bin\java.exe;C:\Program Files\Java\jre-17\bin\java.exe;C:\Program Files\Java\jre-11\bin\java.exe"

REM Try to find Java using where command (searches in PATH)
where java >nul 2>nul
if %errorlevel%==0 (
    for /f "delims=" %%i in ('where java') do (
        set "JAVA=%%i"
        goto :found
    )
)

REM Try common paths if where failed
for %%p in (%JAVA_PATHS%) do (
    if exist "%%p" (
        set "JAVA=%%p"
        goto :found
    )
)

:found
if not defined JAVA (
    echo.
    echo ================================================
    echo  ERROR: Java no encontrado
    echo ================================================
    echo.
    echo Para usar War Era Launcher necesitas tener instalado:
    echo   - JDK 17, JDK 11 o JDK 8
    echo   - O JRE 17 o JRE 11
    echo.
    echo Descarga Java desde:
    echo   https://www.oracle.com/java/technologies/downloads/
    echo.
    echo ================================================
    pause
    exit /b 1
)

cd /d "%~dp0"
start "" "%JAVA%" -jar "WarEraExplorer.jar"
