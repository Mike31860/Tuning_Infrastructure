#!/bin/bash

# Run the Main class with any arguments passed to this script
if [ ! -d "classes" ]; then
    echo "Classes directory not found. Please run compile.sh first."
    exit 1
fi

echo "Running Main class..."
java -cp classes app.Main "$@"