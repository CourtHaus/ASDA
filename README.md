# ASDA - Autism Spectrum Disorder Assessment

[![Java Version](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
[![JavaFX](https://img.shields.io/badge/JavaFX-21.0.5-blue.svg)](https://openjfx.io/)
[![Maven](https://img.shields.io/badge/Maven-3.x-red.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-Educational-green.svg)]()

A desktop application implementing the Autism Quotient (AQ) assessment tool, developed as part of UMGC CMSC 495 Capstone project. The application provides both GUI and CLI interfaces for conducting autism spectrum disorder screening assessments.

## Table of Contents

- [About the Project](#about-the-project)
- [Features](#features)
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

ASDA (Autism Spectrum Disorder Assessment) is a JavaFX-based desktop application that implements the standardized AQ (Autism Quotient) assessment questionnaire. The application is designed to help identify traits associated with autism spectrum conditions through a scientifically validated 50-question assessment.

The AQ assessment was developed by Simon Baron-Cohen and colleagues at the Autism Research Centre, University of Cambridge. It provides a quantitative measure of autistic traits in adults of average intelligence.

## Features

- **Dual Interface Support**: Both graphical (JavaFX) and command-line interfaces
- **50-Question AQ Assessment**: Complete implementation of the standard AQ questionnaire
- **Automated Scoring**: Real-time calculation and interpretation of assessment results
- **Session Persistence**: SQLite database for storing assessment sessions and responses
- **User-Friendly Navigation**: Intuitive flow with disclaimer, questionnaire, and results screens
- **Score Interpretation**: Clear interpretation of results based on established clinical thresholds
- **Cross-Platform**: Runs on Windows, macOS, and Linux

## Technologies Used

### Core Technologies
- **Java 21**: Latest LTS version with modern language features
- **JavaFX 21.0.5**: Rich UI framework for desktop applications
- **Maven**: Project management and build automation

### Dependencies
- **SQLite JDBC (3.43.2.2)**: Lightweight embedded database
- **Jackson (2.17.2)**: JSON processing library
- **ControlsFX (11.2.1)**: Extended JavaFX controls
- **Ikonli (12.3.1)**: Icon packs for JavaFX (Material Design 2)
- **JUnit 5 (5.11.0)**: Unit testing framework

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

The GUI provides:
- **Home Screen**: Application introduction and start button
- **Disclaimer Screen**: Important information about the assessment
- **Questionnaire Screen**: Interactive 50-question assessment
- **Results Screen**: Score display with interpretation

### CLI Mode

Run the assessment in terminal/command-line mode:

```bash
mvn compile exec:java -Dexec.mainClass="org.group1.asda.App" -Dexec.args="--cli"
```

The CLI mode provides a text-based interactive assessment suitable for terminal environments.

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
│   │   │       │   └── Question.java
│   │   │       ├── navigation/                   # UI navigation
│   │   │       │   └── Router.java
│   │   │       ├── persistence/                  # Data access layer
│   │   │       │   ├── Database.java             # Database initialization
│   │   │       │   ├── QuestionDao.java          # Question data access
│   │   │       │   ├── ResponseDao.java          # Response data access
│   │   │       │   └── SessionDao.java           # Session data access
│   │   │       ├── service/                      # Business logic
│   │   │       │   ├── AssessmentService.java
│   │   │       │   └── QuestionnaireService.java
│   │   │       └── ui/                           # UI controllers
│   │   │           ├── disclaimer/
│   │   │           ├── home/
│   │   │           ├── questionnaire/
│   │   │           └── results/
│   │   └── resources/
│   │       └── fxml/                             # FXML view definitions
│   │           ├── disclaimer.fxml
│   │           ├── home.fxml
│   │           ├── questionnaire.fxml
│   │           └── results.fxml
│   └── test/
│       └── java/                                 # Unit tests
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

The project uses JUnit 5 for unit testing. Run tests with:

```bash
mvn test
```

Test reports are generated in the `target/surefire-reports` directory.

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

**Disclaimer**: This application is intended for educational and screening purposes only. It is not a diagnostic tool and should not replace professional medical advice, diagnosis, or treatment. If you have concerns about autism spectrum disorder, please consult with a qualified healthcare professional.
