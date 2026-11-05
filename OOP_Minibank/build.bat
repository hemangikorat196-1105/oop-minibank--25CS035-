@echo off
setlocal
if exist out rmdir /s /q out
mkdir out
if not exist target mkdir target
dir /s /b src\main\java\*.java > sources.txt
javac --release 17 -d out @sources.txt
if errorlevel 1 exit /b 1
jar --create --file target\oop-minibank-1.0.0.jar --manifest MiniBank.mf -C out .
del sources.txt
echo Build successful.
echo Run with: java -jar target\oop-minibank-1.0.0.jar
