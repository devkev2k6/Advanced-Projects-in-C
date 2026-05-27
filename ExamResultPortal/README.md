# College Exam & Result Portal

A console-based **College Exam and Result Management Portal** built in **Java** demonstrating core Object-Oriented Programming concepts with a clean layered architecture.

---

## OOP Concepts Used

| Concept | Where |
|---|---|
| **Abstraction** | `User` abstract class + `Repository<T,ID>` generic interface |
| **Encapsulation** | Private fields with getters/setters across all model classes |
| **Inheritance** | `Student` and `Examiner` extend `User` |
| **Polymorphism** | `getRole()` overridden in both subclasses; `Grade.fromPercentage()` |
| **Generics** | `Repository<T, ID>` — one interface, five implementations |
| **Enums** | `Grade` (with grade points), `ExamType` (with total marks), `ResultStatus` |
| **Custom Exceptions** | `NotFoundException`, `DuplicateEntryException`, `InvalidDataException`, `ResultNotPublishedException` |
| **Composition** | `MarkSheet` composes `Exam`; `SemesterResult` composes `MarkSheet` list |

---

## Features

- Register students and examiners
- Add subjects with credits and semester mapping
- Schedule exams (Mid-Term, End-Term, Practical, Viva, Assignment)
- Enter marks individually or in batch for an entire semester
- Marks auto-converted to Grade + ResultStatus (Pass/Fail/Absent)
- **Result publish/withhold control** — students only see published results
- SGPA calculation (credit-weighted, per semester)
- CGPA calculation (average SGPA across all semesters)
- Semester rank list sorted by SGPA
- Formatted reports: marksheet, semester result, exam results

---

## Project Structure

```
src/
├── main/java/erp/
│   ├── Main.java
│   ├── model/
│   │   ├── User.java              ← abstract base class
│   │   ├── Student.java           ← extends User
│   │   ├── Examiner.java          ← extends User
│   │   ├── Subject.java
│   │   ├── Exam.java
│   │   ├── MarkSheet.java
│   │   ├── SemesterResult.java
│   │   ├── Grade.java             ← enum with grade points
│   │   ├── ExamType.java          ← enum with total marks per type
│   │   └── ResultStatus.java      ← enum: PASS / FAIL / ABSENT / WITHHELD
│   ├── repository/
│   │   ├── Repository.java        ← generic CRUD interface
│   │   ├── StudentRepository.java
│   │   ├── ExaminerRepository.java
│   │   ├── SubjectRepository.java
│   │   ├── ExamRepository.java
│   │   └── MarkSheetRepository.java
│   ├── service/
│   │   ├── StudentService.java
│   │   ├── ExaminerService.java
│   │   ├── SubjectService.java
│   │   ├── ExamService.java
│   │   ├── MarkSheetService.java
│   │   └── ReportService.java
│   ├── exception/
│   │   ├── NotFoundException.java
│   │   ├── DuplicateEntryException.java
│   │   ├── InvalidDataException.java
│   │   └── ResultNotPublishedException.java
│   ├── util/
│   │   ├── Validator.java
│   │   ├── IDGenerator.java
│   │   └── ConsolePrinter.java
│   └── ui/
│       └── ConsoleMenu.java
└── test/java/erp/
    ├── StudentServiceTest.java
    └── MarkSheetServiceTest.java
```

---

## How to Run

### Prerequisites
- Java 17+
- Maven 3.8+

### Build & Run

```bash
mvn clean package
java -jar target/exam-portal.jar
```

### Without Maven

```bash
find src/main/java -name "*.java" > sources.txt
javac -d out @sources.txt
java -cp out erp.Main
```

---

## Sample Workflow

```
1. Examiner Management → Add examiner
2. Subject Management  → Add subjects (with semester + credits)
3. Student Management  → Register students
4. Exam Management     → Schedule exam (links subject + examiner)
5. Marks Entry         → Enter marks (or batch entry for a whole semester)
6. Exam Management     → Publish result (makes it visible to students)
7. Reports             → Student marksheet / Semester result / Rank list
```

---

## Tech Stack

- Java 17
- Maven
- JUnit 5
- No external frameworks — pure Java OOP
