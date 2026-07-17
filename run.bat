@echo off
cd /d "%~dp0"
if not exist out mkdir out
javac -d out src\dentalcare\Main.java
java -cp out dentalcare.Main
