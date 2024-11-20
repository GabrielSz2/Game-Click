@echo off

set EXECUTABLE_PATH=%USERPROFILE%\Desktop\executable

set JAVAFX_PATH=%EXECUTABLE_PATH%\javafx-sdk-23.0.1\lib

cd /d "%EXECUTABLE_PATH%"

java --module-path "%JAVAFX_PATH%" --add-modules javafx.controls,javafx.fxml -jar Game-Click.jar
