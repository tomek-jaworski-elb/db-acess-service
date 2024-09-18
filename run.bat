@echo off
REM Batch Script to Check Java Version and Run the db-access-service

echo =====================================
echo Checking for Java installation...
echo =====================================
java -version

IF %ERRORLEVEL% NEQ 0 (
    echo Error: Java is not installed or not configured correctly.
    echo Please install Java or check your environment variables.
    pause
    exit /b 1
)

echo =====================================
echo Running db-access-service...
echo =====================================
java -jar target/db-access-service-1.1.jar

IF %ERRORLEVEL% NEQ 0 (
    echo Error: Failed to start db-access-service.
    echo Please check the jar file and try again.
    pause
    exit /b 1
)

echo =====================================
echo db-access-service is running successfully.
echo =====================================
pause
