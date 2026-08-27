@ECHO OFF
SETLOCAL
SET "BASE_DIR=%~dp0"
SET "MAVEN_VERSION=3.9.9"
SET "MAVEN_DIR=%BASE_DIR%.mvn\apache-maven-%MAVEN_VERSION%"

IF EXIST "%MAVEN_DIR%\bin\mvn.cmd" GOTO runMaven
IF NOT EXIST "%BASE_DIR%.mvn" MKDIR "%BASE_DIR%.mvn"
SET "ARCHIVE=%BASE_DIR%.mvn\apache-maven-%MAVEN_VERSION%-bin.zip"
PowerShell -NoProfile -ExecutionPolicy Bypass -Command "Invoke-WebRequest -UseBasicParsing -Uri 'https://archive.apache.org/dist/maven/maven-3/%MAVEN_VERSION%/binaries/apache-maven-%MAVEN_VERSION%-bin.zip' -OutFile '%ARCHIVE%'"
IF ERRORLEVEL 1 EXIT /B %ERRORLEVEL%
PowerShell -NoProfile -ExecutionPolicy Bypass -Command "Expand-Archive -LiteralPath '%ARCHIVE%' -DestinationPath '%BASE_DIR%.mvn' -Force"
IF ERRORLEVEL 1 EXIT /B %ERRORLEVEL%
DEL /Q "%ARCHIVE%"

:runMaven
CALL "%MAVEN_DIR%\bin\mvn.cmd" %*
ENDLOCAL
