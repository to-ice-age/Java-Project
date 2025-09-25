# Java-Project

# Campus Course & Records Manager (CCRM)

A comprehensive Java SE console application for managing student records, courses, enrollments, and academic data in educational institutions.

## 📋 Project Overview

The Campus Course & Records Manager (CCRM) is a robust console-based Java application that demonstrates advanced Java SE concepts including OOP principles, design patterns, file I/O with NIO.2, and modern Java features. The system enables educational institutions to efficiently manage students, courses, enrollments, grades, and academic records.

## 🚀 How to Run

### Prerequisites
- **Java Development Kit (JDK) 17 or higher**
- **Maven 3.6+** (optional, if using Maven build)
- **Eclipse IDE** or any Java IDE

### Running the Application

#### Method 1: Using Maven

Clone the repository

git clone https://github.com/to-ice-age/ccrm.git
cd ccrm
Compile and run

mvn compile exec:java -Dexec.mainClass="edu.ccrm.CCRMApplication"
Run with assertions enabled

mvn compile exec:java -Dexec.mainClass="edu.ccrm.CCRMApplication" -Dexec.args="-ea"


#### Method 2: Using Java directly
Compile

javac -cp src/main/java src/main/java/edu/ccrm/CCRMApplication.java
Run with assertions enabled

java -ea -cp src/main/java edu.ccrm.CCRMApplication

## 📚 Java Evolution Timeline

- **1991**: James Gosling begins developing Oak at Sun Microsystems
- **1995**: Java 1.0 released - "Write Once, Run Anywhere"
- **1997**: Java 1.1 - Inner classes, JavaBeans, JDBC
- **1998**: Java 2 (J2SE 1.2) - Collections Framework, Swing
- **2000**: J2SE 1.3 - HotSpot JVM, JavaSound
- **2002**: J2SE 1.4 - Regular expressions, NIO, Logging API
- **2004**: Java 5 - Generics, Annotations, Enums, Autoboxing
- **2006**: Java 6 - Scripting support, JDBC 4.0
- **2011**: Java 7 - Try-with-resources, Diamond operator, NIO.2
- **2014**: Java 8 - Lambda expressions, Streams API, Date/Time API
- **2017**: Java 9 - Modules, JShell
- **2018**: Java 10 - Local variable type inference (var)
- **2018**: Java 11 - LTS version, String methods, HTTP Client
- **2021**: Java 17 - LTS version, Sealed classes, Pattern matching
- **2023**: Java 21 - LTS version, Virtual threads, Record patterns

## 🏗️ Java Platform Comparison

| Feature | Java ME | Java SE | Java EE |
|---------|---------|---------|---------|
| **Target** | Mobile/Embedded devices | Desktop/Standalone apps | Enterprise web applications |
| **Size** | Minimal footprint | Full-featured | Extended enterprise features |
| **APIs** | Limited mobile APIs | Core Java APIs | Web services, JPA, CDI, JSF |
| **Examples** | Mobile apps, IoT devices | Desktop apps, utilities | Web apps, microservices |
| **Memory** | <1MB | 10-100MB | 100MB+ |

## 🔧 Java Architecture

### JDK (Java Development Kit)
- **Purpose**: Complete development environment
- **Contains**: JRE + Development tools (javac, javadoc, jar, etc.)
- **Usage**: Required for developing Java applications

### JRE (Java Runtime Environment)  
- **Purpose**: Runtime environment for executing Java applications
- **Contains**: JVM + Core libraries + Supporting files
- **Usage**: Required for running Java applications

### JVM (Java Virtual Machine)
- **Purpose**: Executes Java bytecode
- **Features**: Platform independence, memory management, garbage collection
- **Interaction**: JDK compiles .java → .class bytecode → JVM executes

**Flow**: Source Code → JDK (javac) → Bytecode → JVM → Machine Code → Execution

## 💻 Windows Installation & Setup

### Java Installation Steps
1. Download JDK 17+ from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://openjdk.org/)
2. Run the installer as administrator
3. Set JAVA_HOME environment variable to JDK installation directory
4. Add %JAVA_HOME%\bin to PATH environment variable
5. Verify installation: `java -version` and `javac -version`

### Eclipse IDE Setup
1. Download Eclipse IDE for Java Developers
2. Extract and launch Eclipse
3. Create new workspace
4. File → New → Java Project
5. Configure project settings (JDK version, build path)
6. Add source folders and packages
7. Configure run configurations with main class

## 🏛️ Project Architecture

### Package Structure
edu.ccrm/
├── CCRMApplication.java (Main class)
├── cli/ (Console interface)
├── domain/ (Entity classes)│
├── Person.java (Abstract base class)
│ ├── Student.java (Student entity)
│ ├── Instructor.java (Instructor entity)
│ ├── Course.java (Course entity with Builder)
│ ├── Enrollment.java (Enrollment entity)
│ ├── Name.java (Immutable value class)
│ ├── Email.java (Immutable value class)
│ ├── Grade.java (Enum with grade points)
│ ├── Semester.java (Enum for semesters)
│ └── StudentStatus.java (Status enum)
├── service/ (Business logic)
│ ├── StudentService.java
│ ├── CourseService.java
│ ├── EnrollmentService.java
│ └── TranscriptService.java
├── io/ (File operations - NIO.2)
│ ├── ImportExportService.java
│ ├── BackupService.java
│ └── FileParser.java
├── util/ (Utilities & helpers)
│ ├── ValidationUtils.java
│ ├── DateTimeUtils.java
│ └── RecursiveFileUtils.java
├── config/ (Configuration)
│ └── AppConfig.java (Singleton pattern)
└── exception/ (Custom exceptions)
├── DuplicateEnrollmentException.java
└── MaxCreditLimitExceededException.java

## 🎯 Technical Requirements Mapping

| Requirement | Implementation | File Location |
|-------------|----------------|---------------|
| **Abstract Class** | Person (abstract base) | `domain/Person.java` |
| **Inheritance** | Student/Instructor extend Person | `domain/Student.java`, `domain/Instructor.java` |
| **Polymorphism** | Person references, overridden methods | Throughout domain classes |
| **Encapsulation** | Private fields + getters/setters | All domain classes |
| **Interfaces** | Persistable, Searchable<T> | `service/` package |
| **Singleton** | AppConfig pattern | `config/AppConfig.java` |
| **Builder** | Course.Builder, Transcript.Builder | `domain/Course.java` |
| **Immutable Class** | Name, Email value objects | `domain/Name.java`, `domain/Email.java` |
| **Enums** | Grade, Semester, StudentStatus | `domain/` package |
| **Custom Exceptions** | Business rule violations | `exception/` package |
| **NIO.2 File I/O** | Path, Files APIs | `io/` package |
| **Streams API** | Data filtering, aggregation | `service/` implementations |
| **Date/Time API** | LocalDateTime for timestamps | Domain entities |
| **Lambdas** | Comparators, Predicates | Service layer filtering |
| **Nested Classes** | Static nested, Inner classes | `domain/Course.java` |
| **Anonymous Classes** | CLI event handling | `cli/` package |

## ⚙️ Key Features

### Student Management
- Add, update, list, and deactivate students
- Student profiles with comprehensive information
- Enrollment tracking and credit limit validation
- Transcript generation with GPA calculation

### Course Management  
- Course creation with builder pattern
- Search and filter by instructor, department, semester
- Instructor assignment and management
- Credit hour tracking

### Enrollment & Grading
- Business rule enforcement (max 18 credits per semester)
- Grade recording with automatic GPA computation
- Letter grade conversion (S, A, B, C, D, E, F)
- Academic transcript generation

### File Operations (NIO.2)
- CSV import/export functionality
- Backup system with timestamped folders
- Recursive directory size calculation
- Data persistence and recovery

### Advanced Features
- Menu-driven CLI with comprehensive options
- Stream-based reporting (GPA distribution, top students)
- Exception handling with custom business exceptions
- Assertion-based invariant checking

## 🔍 Design Patterns Implemented

### Singleton Pattern
- **Class**: `AppConfig`
- **Purpose**: Global application configuration
- **Implementation**: Thread-safe lazy initialization

### Builder Pattern
- **Classes**: `Course.Builder`, `Transcript.Builder`
- **Purpose**: Complex object construction
- **Benefits**: Readable, flexible object creation

## ⚠️ Exception Handling

### Custom Exceptions
- **DuplicateEnrollmentException**: Prevents duplicate course enrollments
- **MaxCreditLimitExceededException**: Enforces credit limits per semester

### Exception Hierarchy
- **Checked Exceptions**: File I/O operations, data persistence
- **Unchecked Exceptions**: Business rule violations, validation errors

## 📊 Usage Examples

### Enabling Assertions

Enable all assertions

java -ea edu.ccrm.CCRMApplication
Enable assertions for specific package

java -ea:edu.ccrm.domain... edu.ccrm.CCRMApplication

### Sample Operations
1. Start application → AppConfig loads configuration
2. Main Menu: Students / Courses / Enrollment / Reports / Export / Backup
3. Add students and courses
4. Enroll students with credit validation
5. Record grades and generate transcripts
6. Export data and create backups
7. Generate reports using Stream API

## 📁 Test Data

Sample CSV files are provided in the `test-data/` folder:
- `students.csv`: Sample student records
- `courses.csv`: Sample course catalog
- `enrollments.csv`: Sample enrollment data

## 🎥 Demo Flow

1. **Startup**: AppConfig singleton initialization
2. **Menu Navigation**: Switch-based menu system
3. **Student Operations**: CRUD operations with validation
4. **Course Management**: Builder pattern demonstration
5. **Enrollment Process**: Business rule enforcement
6. **Grading System**: Enum-based grade calculation
7. **File Operations**: NIO.2 import/export/backup
8. **Reporting**: Stream API aggregation
9. **Platform Info**: Java SE vs ME vs EE summary

## 🛠️ Development Notes

- **Assertions**: Use `-ea` flag to enable invariant checking
- **File Encoding**: UTF-8 throughout the project
- **Java Version**: Requires JDK 17+ for modern language features
- **Memory**: Recommended 512MB heap size for large datasets

## 📸 Screenshots

Screenshots demonstrating the application are available in the ⁠ screenshots/ ⁠ folder:
•⁠  ⁠Java installation verification
•⁠  ⁠Eclipse project setup
•⁠  ⁠Application menu and operations
•⁠  ⁠File export and backup structure


## 🤝 Academic Integrity

This project represents original work demonstrating comprehensive Java SE concepts. All external references and inspirations are acknowledged in the implementation comments.

---

**© 2025 Campus Course & Records Manager - Academic Project**
