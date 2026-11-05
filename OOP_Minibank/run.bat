@echo off
if not exist target\oop-minibank-1.0.0.jar call build.bat
java -jar target\oop-minibank-1.0.0.jar
