# ASDA - Autism Spectrum Disorder Assessment

[![Java Version](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
[![JavaFX](https://img.shields.io/badge/JavaFX-21.0.5-blue.svg)](https://openjfx.io/)
[![Maven](https://img.shields.io/badge/Maven-3.x-red.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-Educational-green.svg)]()
[![Tests](https://img.shields.io/badge/Tests-200%20Passing-brightgreen.svg)]()

A comprehensive desktop application implementing the Autism Quotient (AQ) assessment tool along with cognitive attention and memory games, plus emotional response and recognition modules. Built with JavaFX as a multi-scene app (disclosure → home → tutorials/games/results) with a CLI fallback for the AQ assessment.

## Table of Contents

- [About the Project](#about-the-project)
- [Features](#features)
- [Cognitive Assessment Games](#cognitive-assessment-games)
  - [Memory Matching Game](#memory-matching-game)
  - [Concentration & Attention Game](#concentration--attention-game)
  - [Emotional Survey & Recognition](#emotional-survey--recognition)
- [Technologies Used](#technologies-used)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
  - [GUI Mode](#gui-mode)
  - [CLI Mode](#cli-mode)
- [Project Structure](#project-structure)
- [Database](#database)
- [Building and Running](#building-and-running)
- [Testing](#testing)
- [Contributors](#contributors)
- [License](#license)
- [Acknowledgments](#acknowledgments)

## About the Project

ASDA (Autism Spectrum Disorder Assessment) is a comprehensive JavaFX-based desktop application that combines standardized assessment tools with interactive cognitive games. The application includes:

1. **AQ Assessment**: The standardized Autism Quotient (AQ) questionnaire developed by Simon Baron-Cohen and colleagues at the Autism Research Centre, University of Cambridge. It provides a quantitative measure of autistic traits in adults of average intelligence.

2. **Cognitive Games**: Interactive games designed to assess attention, memory, and emotional processing:
   - **Memory Matching Game**: Progressive difficulty memory card game
   - **Concentration & Attention Game**: Rapid stimulus-response attention test
   - **Emotional Survey & Recognition**: Mood response slider survey plus facial emotion recognition game

These tools work together to provide a more comprehensive view of cognitive functioning and attention patterns.

## Features

### Assessment Tools
- **50-Question AQ Assessment**: Complete implementation of the standard AQ questionnaire
- **Automated Scoring**: Real-time calculation and interpretation of assessment results
- **Session Persistence**: SQLite database for storing assessment sessions and responses
- **Score Interpretation**: Clear interpretation of results based on established clinical thresholds

### Cognitive Games
- **Memory Matching Game**: Up to 5 progressive rounds testing visual memory and pattern recognition
  - Starts with 2 pairs, scales difficulty each round (capped to keep header/footer visible)
  - Tracks accuracy, completion time, and memory scores
  - Provides Attention Performance Index (API) with interpretations

- **Concentration & Attention Game**: 50-trial rapid attention test
  - Press SPACE when current stimulus matches previous (shape + color)
  - 1.3-second stimulus display with 0.7-second intervals
  - Measures sustained attention and response accuracy
  - Provides performance recommendations based on accuracy

- **Emotional Survey & Recognition**:
  - Slider-based emotional response survey for abstract patterns
  - Facial emotion recognition game with visual stimuli and multiple-choice responses
  - Results summarized with colorful bar breakdowns and interpretation

### Interface & Usability
- **Dual Interface Support**: Both graphical (JavaFX) and command-line interfaces for AQ assessment
- **User-Friendly Navigation**: Intuitive menu with easy access to all features
- **Visual Feedback**: Color-coded responses and clear result displays
- **Cross-Platform**: Runs on Windows, macOS, and Linux

### Technical Features
- **Comprehensive Testing**: 200+ unit tests ensuring reliability
- **Modular Architecture**: Clean separation of concerns with MVC pattern
- **Null-Safe Operations**: Robust error handling throughout

## Cognitive Assessment Games

### Memory Matching Game

A progressive memory card game with controlled difficulty to keep the layout stable.

- **Up to 5 Rounds**: Starts with 2 pairs and scales up; rounds capped at 5 to keep UI readable
- **Preview Phase**: Cards show face-up for 3 seconds
- **Gameplay**: Flip to find matching shape+color pairs; first-time mismatches are forgiven
- **Scoring**: Tracks correct/incorrect, attempts, accuracy, and memory score with API interpretation
- **Results**: Summaries plus API bands (High Focus / Moderate / Below Average / Significant Variance)

### Concentration & Attention Game

Rapid stimulus-response test measuring sustained attention.

- **50 Trials**: Sequence of colored shapes (circles, squares, triangles)
- **Task**: Press SPACE when current stimulus matches previous (shape + color)
- **Timing**: 1.3s display + 0.7s interval
- **Results**: Accuracy %, correct/missed counts, and recommendations based on performance

### Emotional Survey & Recognition

Two-part emotional module:

- **Emotional Survey**: Rate mood response (1–5 slider) for abstract art patterns; results show average mood and positive/neutral/tense bar breakdowns.
- **Emotion Recognition Game**: Identify facial expressions from images; results include accuracy bars, correct/missed breakdown, and qualitative feedback.

## Technologies Used

### Core Technologies
- **Java 21**: Latest LTS version with modern language features
- **JavaFX 21.0.5**: Rich UI framework for desktop applications
  - Canvas API for custom shape rendering
  - Animation API for smooth transitions
- **Maven**: Project management and build automation

### Dependencies
- **SQLite JDBC**: Embedded database
- **Jackson**: JSON processing
- **ControlsFX / Ikonli**: JavaFX controls and icons
- **JUnit 5**: Testing

## Prerequisites

Before running this application, ensure you have the following installed:

- **Java Development Kit (JDK) 21** or higher
  - [Download JDK 21](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
  - Verify installation: `java -version`

- **Apache Maven 3.x** or higher
  - [Download Maven](https://maven.apache.org/download.cgi)
  - Verify installation: `mvn -version`

## Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd ASDA
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Verify the build**
   ```bash
   mvn test
   ```

The application will automatically initialize the SQLite database on first run, creating the schema and seeding the AQ questions.

## Usage

### GUI Mode

Run the application with the graphical interface:

```bash
mvn javafx:run
```

Flow: Loading screen → Disclosure → Home tiles → Tutorial (per game) → Game/Survey → Results.

**Home Screen Options:**
1. **Start Questionnaire**: 50-question AQ assessment with progress and results interpretation.
2. **Matching Game**: 5-round capped memory game with preview and API-based results.
3. **Attention Game**: 50-trial concentration test with accuracy and recommendations.
4. **Emotional Survey & Recognition**: Mood slider survey and facial emotion recognition; results include bar charts and feedback.

### CLI Mode

Run the AQ assessment in terminal/command-line mode:

```bash
mvn compile exec:java -Dexec.mainClass="org.group1.asda.App" -Dexec.args="--cli"
```

The CLI mode provides a text-based interactive AQ assessment suitable for terminal environments.

**Note**: The cognitive games require the GUI interface due to their visual and interactive nature.

## Project Structure

```
ASDA/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/group1/asda/
│   │   │       ├── App.java                      # Main application entry point (loading → disclosure → home)
│   │   │       ├── assessment/                   # AQ assessment logic (CLI + GUI)
│   │   │       ├── domain/                       # Domain models (AQ, matching, attention, emotional)
│   │   │       ├── navigation/Router.java        # Scene routing and stylesheets
│   │   │       ├── persistence/                  # Database init + DAOs
│   │   │       ├── service/                      # Business logic
│   │   │       └── ui/                           # UI controllers grouped by feature:
│   │   │           ├── attentiongame/            # Attention game + results
│   │   │           ├── matchinggame/             # Matching game + tutorial/results
│   │   │           ├── emotionalsurvey/          # Emotional survey + results
│   │   │           ├── emotionrecognition/       # Recognition game + results
│   │   │           ├── home/                     # Home screen
│   │   │           └── disclaimer/               # Disclosure screen
│   │   └── resources/
│   │       ├── css/                              # Stylesheets per screen (home, games, results, disclosure)
│   │       └── fxml/                             # FXML view definitions (loading, disclosure, home, games, results)
│   │           ├── home.fxml                     # Main menu
│   │           ├── matching-game.fxml            # Matching game view
│   │           ├── questionnaire.fxml
│   │           └── results.fxml
│   └── test/
│       └── java/                                 # Unit tests (200+ tests)
│           └── org/group1/asda/
│               ├── assessment/                   # AQ assessment tests
│               │   ├── AQAssessmentTest.java
│               │   ├── AQQuestionTest.java
│               │   ├── AQResultInterpreterTest.java
│               │   └── AQScoreCalculatorTest.java
│               ├── domain/                       # Domain model tests
│               │   ├── AttentionGameStateTest.java (31 tests)
│               │   ├── CardTest.java             (21 tests)
│               │   ├── GameStateTest.java        (26 tests)
│               │   ├── QuestionTest.java         (10 tests)
│               │   └── StimulusTest.java         (26 tests)
│               └── service/                      # Service layer tests
│                   └── AssessmentServiceTest.java
├── pom.xml                                       # Maven configuration
├── README.md
└── .gitignore
```

## Database

The application uses **SQLite** as an embedded database. The database file (`asda.db`) is automatically created in the project root directory on first run.

### Schema

- **questions**: Stores the 50 AQ assessment questions
- **sessions**: Tracks individual assessment sessions
- **responses**: Stores user responses for each session

The database is initialized and seeded automatically by the `Database.init()` method.

## Building and Running

### Compile the project
```bash
mvn compile
```

### Run tests
```bash
mvn test
```

### Package the application
```bash
mvn package
```

### Clean build artifacts
```bash
mvn clean
```

### Run with Maven
```bash
# GUI mode
mvn javafx:run

# CLI mode
mvn compile exec:java -Dexec.mainClass="org.group1.asda.App" -Dexec.args="--cli"
```

## Testing

The project uses **JUnit 5** for comprehensive unit testing with over **200 tests** ensuring reliability and correctness.

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=CardTest

# Run with detailed output
mvn test -X
```

### Test Coverage

**200 Total Tests** across multiple components:

#### AQ Assessment Tests (86 tests)
- `AQAssessmentTest.java` (14 tests): Core assessment logic
- `AQQuestionTest.java` (9 tests): Question handling
- `AQResultInterpreterTest.java` (28 tests): Score interpretation
- `AQScoreCalculatorTest.java` (11 tests): Scoring algorithms
- `AssessmentServiceTest.java` (24 tests): Service layer

#### Matching Game Tests (47 tests)
- `CardTest.java` (21 tests): Card matching logic, color/shape handling, null safety
- `GameStateTest.java` (26 tests): Round management, scoring, deck generation, API calculations

#### Attention Game Tests (57 tests)
- `StimulusTest.java` (26 tests): Stimulus matching, color precision, edge cases
- `AttentionGameStateTest.java` (31 tests): Statistics tracking, accuracy calculations, performance interpretations

#### Domain Tests (10 tests)
- `QuestionTest.java` (10 tests): Question record validation

### Test Reports

Test reports are automatically generated in:
- **Location**: `target/surefire-reports/`
- **Format**: XML and text summaries
- **Coverage**: Line and branch coverage metrics

### Test Features

✓ **Comprehensive Coverage**: All domain models and game logic
✓ **Edge Case Testing**: Null handling, boundary values
✓ **State Management**: Game state tracking and transitions
✓ **Accuracy Validation**: Score and accuracy calculations
✓ **Performance Testing**: Timer and duration tracking
✓ **Null-Safe Operations**: Robust error handling verification

## Contributors

**UMGC CMSC 495 - Group 1**

This project was developed as part of the Computer Science Capstone course at University of Maryland Global Campus.

## License

This project is developed for educational purposes as part of a university capstone project. All rights reserved to the project contributors and UMGC.

## Acknowledgments

- **AQ Assessment**: Developed by Simon Baron-Cohen and colleagues at the Autism Research Centre, University of Cambridge
- **UMGC CMSC 495**: Computer Science Capstone course
- **OpenJFX**: JavaFX community and maintainers
- **Maven**: Apache Maven project team

---

## Screenshots and Demo

### Home Screen
The main menu provides access to all assessment tools:
- AQ Questionnaire Assessment
- Memory Matching Game
- Concentration & Attention Game

### Game Interfaces
- **Matching Game**: Colorful cards with shapes (circles, squares, triangles) in pastel colors
- **Attention Game**: Rapid stimulus presentation with real-time feedback

---

**Disclaimer**: This application is intended for educational, research, and screening purposes only. Neither the AQ assessment nor the cognitive games are diagnostic tools and should not replace professional medical advice, diagnosis, or treatment. The cognitive games measure attention and memory performance but do not diagnose any medical or psychological condition. If you have concerns about autism spectrum disorder, ADHD, or other conditions, please consult with a qualified healthcare professional for proper evaluation and diagnosis.
