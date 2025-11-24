# Java Project - TuningSystem_Back_End_Sections

This is a normal Java project (non-Maven) with a simple directory structure.

## Project Structure

```
TuningSystem_Back_End_Sections/
├── java/                    # Source code directory
│   ├── app/                # Main application package
│   │   └── Main.java      # Main class
│   └── Entities/          # Entity classes
│       ├── Loop.java
│       ├── Procedure.java
│       ├── Section.java
│       ├── Technique.java
│       └── Transformation.java
├── classes/               # Compiled classes (created after compilation)
├── compile.sh            # Compilation script
├── run.sh               # Execution script
└── README.md           # This file
```

## How to Build and Run

### Option 1: Using the provided scripts (Recommended)

1. **Compile the project:**
   ```bash
   ./compile.sh
   ```

2. **Run the program:**
   ```bash
   ./run.sh [arguments]
   ```
   Example:
   ```bash
   ./run.sh 3 Poly someApplication className tuningFlag loopBegining windowsSize
   ```

### Option 2: Manual compilation and execution

1. **Compile manually:**
   ```bash
   mkdir -p classes
   javac -d classes -cp java java/app/*.java java/Entities/*.java
   ```

2. **Run manually:**
   ```bash
   java -cp classes app.Main [arguments]
   ```

## Required Arguments

The Main class expects the following arguments:
1. algorithm (e.g., "3")
2. benchMark (e.g., "Poly" or "Nasa")
3. nameApplication
4. className
5. tuningFlag
6. loopBegining
7. windowsSize

## Notes

- Make sure you have Java installed on your system
- The compiled `.class` files will be stored in the `classes/` directory
- This project structure is simpler than Maven and doesn't require any build tool# Tuning_Infrastructure
# Tuning_Infrastructure
# Tuning_Infrastructure
# Tuning_Infrastructure
