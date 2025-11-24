#!/bin/bash

# Compile all Java files
echo "Compiling Java files..."

# Create output directory for compiled classes
mkdir -p classes

# Compile all Java files with the source files in the classpath
javac -d classes -cp java java/app/*.java java/Entities/*.java

if [ $? -eq 0 ]; then
    echo "Compilation successful!"
    echo "To run the program, use: java -cp classes app.Main [arguments]"
else
    echo "Compilation failed!"
    exit 1
fi