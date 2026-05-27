package erp.ui;

import erp.model.*;
import erp.repository.*;
import erp.service.*;
import erp.util.ConsolePrinter;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {

    private final StudentService   studentService;
    private final ExaminerService  examinerService;
    private final SubjectService   subjectService;
    private final ExamService      examService;
    private final MarkSheetService markSheetService;
    private final ReportService    reportService;
    private final Scanner          sc;

    public ConsoleMenu() {
        StudentRepository   studentRepo  = new StudentRepository();
        ExaminerRepository  examinerRepo = new ExaminerRepository();
        SubjectRepository   subjectRepo  = new SubjectRepository();
        ExamRepository      examRepo     = new ExamRepository();
        MarkSheetRepository msRepo       = new MarkSheetRepository();

        this.studentService   = new StudentService(studentRepo);
        this.examinerService  = new ExaminerService(examinerRepo);
        this.subjectService   = new SubjectService(subjectRepo);
        this.examService      = new ExamService(examRepo, subjectService, examinerService);
        this.markSheetService = new MarkSheetService(msRepo, examService, studentService);
        this.reportService    = new ReportService(studentService, markSheetService, examService);
        this.sc               = new Scanner(System.in);
    }

    public void start() {
        banner();
        boolean running = true;
        while (running) {
            mainMenu();
            switch (readInt("Choice")) {
                case 1  -> studentMenu();
                case 2  -> examinerMenu();
                case 3  -> subjectMenu();
                case 4  -> examMenu();
                case 5  -> marksMenu();
                case 6  -> reportsMenu();
                case 0  -> { System.out.println("\n  Goodbye!\n"); running = false; }
                default -> ConsolePrinter.error("Invalid option.");
            }
        }
        sc.close();
    }

    // ─── Student ──────────────────────────────────────────────────────────────

    private void studentMenu() {
        boolean back = false;
        while (!back) {
            ConsolePrinter.sectionHeader("STUDENT MANAGEMENT");
            System.out.println("  1. Register student    2. View student");
            System.out.println("  3. List all students   4. Update semester");
            System.out.println("  5. Delete student      0. Back");
            switch (readInt("Choice")) {
                case 1 -> registerStudent();
                case 2 -> viewStudent();
                case 3 -> listStudents();
                case 4 -> updateSemester();
                case 5 -> deleteStudent();
                case 0 -> back = true;
                default -> ConsolePrinter.error("Invalid option.");
            }
        }
    }

    private void registerStudent() {
        try {
            String name       = readStr("Name");
            String email      = readStr("Email");
            String dept       = readStr("Department");
            int    sem        = readInt("Semester (1-8)");
            String batch      = readStr("Batch (e.g. 2022-2026)");
            String rollNumber = readStr("Roll Number");
            Student s = studentService.register(name, email, dept, sem, batch, rollNumber);
            ConsolePrinter.success("Student registered. ID: " + s.getId());
        } catch (Exception e) { ConsolePrinter.error(e.getMessage()); }
    }

    private void viewStudent() {
        try {
            Student s = studentService.findById(readStr("Student ID"));
            System.out.println("\n  " + s);
        } catch (Exception e) { ConsolePrinter.error(e.getMessage()); }
    }

    private void listStudents() {
        List<Student> all = studentService.findAll();
        ConsolePrinter.sectionHeader("ALL STUDENTS");
        if (all.isEmpty()) ConsolePrinter.empty("No students registered.");
        else all.forEach(s -> System.out.println("  " + s));
    }

    private void updateSemester() {
        try {
            String id  = readStr("Student ID");
            int    sem = readInt("New Semester (1-8)");
            studentService.updateSemester(id, sem);
            ConsolePrinter.success("Semester updated.");
        } catch (Exception e) { ConsolePrinter.error(e.getMessage()); }
    }

    private void deleteStudent() {
        try {
            studentService.delete(readStr("Student ID"));
            ConsolePrinter.success("Student removed.");
        } catch (Exception e) { ConsolePrinter.error(e.getMessage()); }
    }

    // ─── Examiner ─────────────────────────────────────────────────────────────

    private void examinerMenu() {
        boolean back = false;
        while (!back) {
            ConsolePrinter.sectionHeader("EXAMINER MANAGEMENT");
            System.out.println("  1. Add examiner    2. List all    3. Delete    0. Back");
            switch (readInt("Choice")) {
                case 1 -> addExaminer();
                case 2 -> listExaminers();
                case 3 -> deleteExaminer();
                case 0 -> back = true;
                default -> ConsolePrinter.error("Invalid option.");
            }
        }
    }

    private void addExaminer() {
        try {
            String name        = readStr("Name");
            String email       = readStr("Email");
            String dept        = readStr("Department");
            String designation = readStr("Designation");
            Examiner e = examinerService.add(name, email, dept, designation);
            ConsolePrinter.success("Examiner added. ID: " + e.getId());
        } catch (Exception e) { ConsolePrinter.error(e.getMessage()); }
    }

    private void listExaminers() {
        List<Examiner> all = examinerService.findAll();
        ConsolePrinter.sectionHeader("ALL EXAMINERS");
        if (all.isEmpty()) ConsolePrinter.empty("No examiners added.");
        else all.forEach(e -> System.out.println("  " + e));
    }

    private void deleteExaminer() {
        try {
            examinerService.delete(readStr("Examiner ID"));
            ConsolePrinter.success("Examiner removed.");
        } catch (Exception e) { ConsolePrinter.error(e.getMessage()); }
    }

    // ─── Subject ──────────────────────────────────────────────────────────────

    private void subjectMenu() {
        boolean back = false;
        while (!back) {
            ConsolePrinter.sectionHeader("SUBJECT MANAGEMENT");
            System.out.println("  1. Add subject    2. List all    0. Back");
            switch (readInt("Choice")) {
                case 1 -> addSubject();
                case 2 -> listSubjects();
                case 0 -> back = true;
                default -> ConsolePrinter.error("Invalid option.");
            }
        }
    }

    private void addSubject() {
        try {
            String prefix  = readStr("Code Prefix (e.g. CS, EC)");
            String name    = readStr("Subject Name");
            int    credits = readInt("Credits (1-6)");
            int    sem     = readInt("Semester (1-8)");
            String dept    = readStr("Department");
            Subject s = subjectService.add(prefix, name, credits, sem, dept);
            ConsolePrinter.success("Subject added. Code: " + s.getSubjectCode());
        } catch (Exception e) { ConsolePrinter.error(e.getMessage()); }
    }

    private void listSubjects() {
        List<Subject> all = subjectService.findAll();
        ConsolePrinter.sectionHeader("ALL SUBJECTS");
        if (all.isEmpty()) ConsolePrinter.empty("No subjects added.");
        else all.forEach(s -> System.out.println("  " + s));
    }

    // ─── Exam ─────────────────────────────────────────────────────────────────

    private void examMenu() {
        boolean back = false;
        while (!back) {
            ConsolePrinter.sectionHeader("EXAM MANAGEMENT");
            System.out.println("  1. Schedule exam       2. Publish result");
            System.out.println("  3. List all exams      0. Back");
            switch (readInt("Choice")) {
                case 1 -> scheduleExam();
                case 2 -> publishResult();
                case 3 -> reportService.printExamSchedule();
                case 0 -> back = true;
                default -> ConsolePrinter.error("Invalid option.");
            }
        }
    }

    private void scheduleExam() {
        try {
            listSubjects();
            String     code       = readStr("Subject Code");
            ExamType   type       = readExamType();
            LocalDate  date       = readDate("Exam Date (YYYY-MM-DD)");
            listExaminers();
            String     examinerId = readStr("Examiner ID");
            Exam e = examService.schedule(code, type, date, examinerId);
            ConsolePrinter.success("Exam scheduled. ID: " + e.getExamId());
        } catch (Exception e) { ConsolePrinter.error(e.getMessage()); }
    }

    private void publishResult() {
        try {
            String id = readStr("Exam ID");
            examService.publishResult(id);
            ConsolePrinter.success("Results published for exam: " + id);
        } catch (Exception e) { ConsolePrinter.error(e.getMessage()); }
    }

    // ─── Marks ────────────────────────────────────────────────────────────────

    private void marksMenu() {
        boolean back = false;
        while (!back) {
            ConsolePrinter.sectionHeader("MARKS ENTRY");
            System.out.println("  1. Enter marks for student    2. View marks by exam");
            System.out.println("  3. Batch enter marks          0. Back");
            switch (readInt("Choice")) {
                case 1 -> enterMarks();
                case 2 -> viewMarksByExam();
                case 3 -> batchEnterMarks();
                case 0 -> back = true;
                default -> ConsolePrinter.error("Invalid option.");
            }
        }
    }

    private void enterMarks() {
        try {
            String studentId = readStr("Student ID");
            String examId    = readStr("Exam ID");
            System.out.print("  Marks obtained (-1 for absent): ");
            double marks = readDouble();
            MarkSheet ms = markSheetService.enterMarks(studentId, examId, marks);
            ConsolePrinter.success("Marks entered. Grade: " + ms.getGrade().getLabel()
                    + " | Status: " + ms.getStatus());
        } catch (Exception e) { ConsolePrinter.error(e.getMessage()); }
    }

    private void viewMarksByExam() {
        try {
            reportService.printExamResults(readStr("Exam ID"));
        } catch (Exception e) { ConsolePrinter.error(e.getMessage()); }
    }

    private void batchEnterMarks() {
        try {
            String examId = readStr("Exam ID");
            Exam exam = examService.findById(examId);
            int    sem     = exam.getSubject().getSemester();
            List<Student> students = studentService.findBySemester(sem);

            if (students.isEmpty()) { ConsolePrinter.empty("No students in semester " + sem); return; }

            System.out.println("  Entering marks for " + students.size() + " students (-1 = absent):");
            for (Student s : students) {
                System.out.printf("  %s %-22s: ", s.getId(), s.getName());
                double marks = readDouble();
                try {
                    markSheetService.enterMarks(s.getId(), examId, marks);
                } catch (Exception ex) {
                    System.out.println("    Skipped: " + ex.getMessage());
                }
            }
            ConsolePrinter.success("Batch entry complete.");
        } catch (Exception e) { ConsolePrinter.error(e.getMessage()); }
    }

    // ─── Reports ──────────────────────────────────────────────────────────────

    private void reportsMenu() {
        boolean back = false;
        while (!back) {
            ConsolePrinter.sectionHeader("REPORTS");
            System.out.println("  1. Student marksheet       2. Semester result");
            System.out.println("  3. Semester rank list      4. Exam results");
            System.out.println("  0. Back");
            switch (readInt("Choice")) {
                case 1 -> reportService.printStudentMarksheet(readStr("Student ID"));
                case 2 -> { String id = readStr("Student ID"); int sem = readInt("Semester"); reportService.printSemesterResult(id, sem); }
                case 3 -> reportService.printRankList(readInt("Semester"));
                case 4 -> reportService.printExamResults(readStr("Exam ID"));
                case 0 -> back = true;
                default -> ConsolePrinter.error("Invalid option.");
            }
        }
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private void banner() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║        COLLEGE EXAM & RESULT PORTAL  v1.0           ║");
        System.out.println("║           Java OOP Project — Console App             ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
        System.out.println();
    }

    private void mainMenu() {
        System.out.println("┌─ MAIN MENU ──────────────────────────────────────┐");
        System.out.println("│  1. Student Management    2. Examiner Management  │");
        System.out.println("│  3. Subject Management    4. Exam Management      │");
        System.out.println("│  5. Marks Entry           6. Reports              │");
        System.out.println("│  0. Exit                                          │");
        System.out.println("└───────────────────────────────────────────────────┘");
    }

    private ExamType readExamType() {
        System.out.println("  Exam Types:");
        ExamType[] types = ExamType.values();
        for (int i = 0; i < types.length; i++)
            System.out.printf("    %d. %s%n", i + 1, types[i]);
        int choice = readInt("Select") - 1;
        if (choice < 0 || choice >= types.length) throw new erp.exception.InvalidDataException("Invalid exam type.");
        return types[choice];
    }

    private LocalDate readDate(String prompt) {
        System.out.print("  " + prompt + ": ");
        try { return LocalDate.parse(sc.nextLine().trim()); }
        catch (DateTimeParseException e) { throw new erp.exception.InvalidDataException("Invalid date format. Use YYYY-MM-DD."); }
    }

    private String readStr(String prompt) {
        System.out.print("  " + prompt + ": ");
        return sc.nextLine().trim();
    }

    private int readInt(String prompt) {
        System.out.print("  " + prompt + ": ");
        try { return Integer.parseInt(sc.nextLine().trim()); }
        catch (NumberFormatException e) { return -1; }
    }

    private double readDouble() {
        try { return Double.parseDouble(sc.nextLine().trim()); }
        catch (NumberFormatException e) { return -1; }
    }
}
