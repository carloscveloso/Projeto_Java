@echo off
cd /d "%~dp0"
if not exist out mkdir out
javac -d out src\dentalcare\*.java src\dentalcare\app\*.java src\dentalcare\config\*.java src\dentalcare\model\*.java src\dentalcare\repository\*.java src\dentalcare\service\*.java
if %ERRORLEVEL% neq 0 exit /b %ERRORLEVEL%
java -cp out dentalcare.Main
