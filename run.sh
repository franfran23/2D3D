#!/bin/bash

echo "Compiling classes..."
javac -sourcepath src src/display/windowMain.java src/geometry/*.java -d classes
echo "Running 2D3D rendering..."
java -classpath classes/ display.windowMain