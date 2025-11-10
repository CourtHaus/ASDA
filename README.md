# ASDA - Autism Spectrum Disorder Assessment

[![Java Version](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
[![JavaFX](https://img.shields.io/badge/JavaFX-21.0.5-blue.svg)](https://openjfx.io/)
[![Maven](https://img.shields.io/badge/Maven-3.x-red.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-Educational-green.svg)]()
[![Tests](https://img.shields.io/badge/Tests-200%20Passing-brightgreen.svg)]()

A comprehensive desktop application implementing the Autism Quotient (AQ) assessment tool along with cognitive attention and memory games, developed as part of UMGC CMSC 495 Capstone project. The application provides both GUI and CLI interfaces for conducting autism spectrum disorder screening assessments and cognitive performance evaluations.

## Table of Contents

- [About the Project](#about-the-project)
- [Features](#features)
- [Cognitive Assessment Games](#cognitive-assessment-games)
  - [Memory Matching Game](#memory-matching-game)
  - [Concentration & Attention Game](#concentration--attention-game)
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

2. **Cognitive Games**: Two interactive games designed to assess attention, memory, and cognitive performance:
   - **Memory Matching Game**: Progressive difficulty memory card game
   - **Concentration & Attention Game**: Rapid stimulus-response attention test

These tools work together to provide a more comprehensive view of cognitive functioning and attention patterns.

## Features

### Assessment Tools
- **50-Question AQ Assessment**: Complete implementation of the standard AQ questionnaire
- **Automated Scoring**: Real-time calculation and interpretation of assessment results
- **Session Persistence**: SQLite database for storing assessment sessions and responses
- **Score Interpretation**: Clear interpretation of results based on established clinical thresholds

### Cognitive Games
- **Memory Matching Game**: 10 progressive rounds testing visual memory and pattern recognition
  - Starts with 2 pairs, increases to 20 cards by round 10
  - Tracks accuracy, completion time, and memory scores
  - Provides Attention Performance Index (API) with interpretations

- **Concentration & Attention Game**: 100-trial rapid attention test
  - Press SPACE when current stimulus matches previous (shape + color)
  - 1.3-second stimulus display with 0.7-second intervals
  - Measures sustained attention and response accuracy
  - Provides performance recommendations based on accuracy

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

A progressive difficulty memory card matching game that tests visual memory and pattern recognition abilities.

**How it works:**
- **10 Rounds**: Starts easy with 2 pairs, progressively increases to 10 pairs (20 cards) by round 10
- **Preview Phase**: Cards are shown face-up for 3 seconds before the game begins
- **Gameplay**: Click to flip cards and find matching pairs (same shape AND color)
- **Scoring System**:
  - Tracks correct matches, incorrect attempts, and accuracy
  - Calculates memory score based on accuracy and completion time
  - First-time mismatches don't count against you
  - Only repeated attempts on previously seen cards affect your score

**Results:**
- Round-by-round performance summaries
- Final Attention Performance Index (API) score
- Interpretation categories:
  - **High Focus (85%+ API)**: No attention concerns indicated
  - **Moderate Focus (70-84% API)**: Minor attention variance detected
  - **Below Average (55-69% API)**: Consider behavioral self-assessment
  - **Significant Variance (<55% API)**: Clinical consultation recommended

### Concentration & Attention Game

A rapid stimulus-response test measuring sustained attention and concentration.

**How it works:**
- **100 Trials**: Sequence of colored shapes (circles, squares, triangles)
- **Task**: Press SPACE when the current stimulus matches the previous one (both shape AND color must match)
- **Timing**: 1.3 seconds display time + 0.7 second blank interval
- **Colors Used**: Light pink and powder blue pastel colors

**Scoring:**
- Correct responses (hits): Pressing SPACE when stimuli match
- Correct withholding: Not pressing when stimuli differ
- Real-time accuracy tracking
- Total completion time measurement

**Results:**
- Accuracy percentage (correct responses / total trials)
- Response patterns analysis
- Performance interpretation:
  - **Excellent (90%+ accuracy)**: Strong sustained attention
  - **Moderate (75-89% accuracy)**: Adequate attention with possible fluctuation
  - **Poor (<75% accuracy)**: Difficulty maintaining attention under rapid stimuli

## Technologies Used

### Core Technologies
- **Java 21**: Latest LTS version with modern language features
- **JavaFX 21.0.5**: Rich UI framework for desktop applications
  - Canvas API for custom shape rendering
  - Animation API for smooth transitions
- **Maven**: Project management and build automation

### Dependencies
- **SQLite JDBC (3.43.2.2)**: Lightweight embedded database
- **Jackson (2.17.2)**: JSON processing library
- **ControlsFX (11.2.1)**: Extended JavaFX controls
- **Ikonli (12.3.1)**: Icon packs for JavaFX (Material Design 2)
- **JUnit 5 (5.11.0)**: Comprehensive unit testing framework

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

The GUI provides access to all features through an intuitive home screen:

**Home Screen Options:**
1. **Start Questionnaire**: Launch the 50-question AQ assessment
   - Disclaimer screen with important information
   - Interactive questionnaire with progress tracking
   - Results screen with score interpretation

2. **Start Matching Game**: Play the progressive memory card game
   - 10 rounds of increasing difficulty
   - Real-time performance tracking
   - Comprehensive results with API score

3. **Start Attention Game**: Take the concentration assessment
   - 100-trial rapid attention test
   - Immediate feedback on performance
   - Detailed accuracy and recommendation report

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
│   │   │       ├── App.java                      # Main application entry point
│   │   │       ├── assessment/                   # AQ assessment logic
│   │   │       │   ├── AQAssessment.java         # Assessment manager
│   │   │       │   ├── AQQuestion.java           # Question model
│   │   │       │   ├── AQScoreCalculator.java    # Scoring logic
│   │   │       │   ├── AQResultInterpreter.java  # Results interpretation
│   │   │       │   └── AssessmentCli.java        # CLI interface
│   │   │       ├── domain/                       # Domain models
│   │   │       │   ├── Question.java             # AQ question model
│   │   │       │   ├── Card.java                 # Matching game card
│   │   │       │   ├── GameState.java            # Matching game state
│   │   │       │   ├── Stimulus.java             # Attention game stimulus
│   │   │       │   └── AttentionGameState.java   # Attention game state
│   │   │       ├── navigation/                   # UI navigation
│   │   │       │   └── Router.java               # Screen routing
│   │   │       ├── persistence/                  # Data access layer
│   │   │       │   ├── Database.java             # Database initialization
│   │   │       │   ├── QuestionDao.java          # Question data access
│   │   │       │   ├── ResponseDao.java          # Response data access
│   │   │       │   └── SessionDao.java           # Session data access
│   │   │       ├── service/                      # Business logic
│   │   │       │   ├── AssessmentService.java
│   │   │       │   └── QuestionnaireService.java
│   │   │       └── ui/                           # UI controllers
│   │   │           ├── attentiongame/            # Attention game UI
│   │   │           │   └── AttentionGameController.java
│   │   │           ├── disclaimer/
│   │   │           ├── home/                     # Main menu
│   │   │           │   └── HomeController.java
│   │   │           ├── matchinggame/             # Matching game UI
│   │   │           │   └── MatchingGameController.java
│   │   │           ├── questionnaire/
│   │   │           └── results/
│   │   └── resources/
│   │       ├── css/                              # Stylesheets
│   │       │   ├── base.css
│   │       │   ├── theme-light.css
│   │       │   └── assessment.css
│   │       └── fxml/                             # FXML view definitions
│   │           ├── attention-game.fxml           # Attention game view
│   │           ├── disclaimer.fxml
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
