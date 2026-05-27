package erp.service;

import erp.model.*;
import erp.util.ConsolePrinter;

import java.util.List;
import java.util.Map;

public class ReportService {

    private final StudentService    studentService;
    private final MarkSheetService  markSheetService;
    private final ExamService       examService;

    public ReportService(StudentService studentService,
                          MarkSheetService markSheetService,
                          ExamService examService) {
        this.studentService   = studentService;
        this.markSheetService = markSheetService;
        this.examService      = examService;
    }

    public void printStudentMarksheet(String studentId) {
        Student s = studentService.findById(studentId);
        List<MarkSheet> marks = markSheetService.getResultsForStudent(studentId);

        ConsolePrinter.header("MARKSHEET — " + s.getName(), 72);
        ConsolePrinter.info("Student ID",  s.getId());
        ConsolePrinter.info("Roll Number", s.getRollNumber());
        ConsolePrinter.info("Department",  s.getDepartment());
        ConsolePrinter.info("Batch",       s.getBatch());
        ConsolePrinter.info("Semester",    String.valueOf(s.getSemester()));
        ConsolePrinter.divider(72);

        if (marks.isEmpty()) {
            ConsolePrinter.empty("No published results available.");
        } else {
            System.out.printf("  %-35s %-13s %-14s %-6s %s%n",
                    "Subject", "Exam Type", "Marks", "Grade", "Status");
            ConsolePrinter.divider(72);
            for (MarkSheet ms : marks) {
                System.out.println("  " + ms);
            }
        }

        ConsolePrinter.divider(72);
        double cgpa = markSheetService.calculateCGPA(studentId, s.getSemester());
        System.out.printf("  CGPA (up to Sem %d): %.2f / 10.00%n", s.getSemester(), cgpa);
        ConsolePrinter.divider(72);
        System.out.println();
    }

    public void printSemesterResult(String studentId, int semester) {
        Student s  = studentService.findById(studentId);
        SemesterResult sr = markSheetService.getSemesterResult(studentId, semester);

        ConsolePrinter.header("SEMESTER " + semester + " RESULT — " + s.getName(), 72);
        if (sr.getMarkSheets().isEmpty()) {
            ConsolePrinter.empty("No published results for Semester " + semester + ".");
        } else {
            for (MarkSheet ms : sr.getMarkSheets()) {
                System.out.println("  " + ms);
            }
            ConsolePrinter.divider(72);
            System.out.printf("  SGPA       : %.2f%n", sr.calculateSGPA());
            System.out.printf("  Overall    : %s%n", sr.isOverallPass() ? "PASS ✔" : "FAIL ✘");
        }
        ConsolePrinter.divider(72);
        System.out.println();
    }

    public void printRankList(int semester) {
        ConsolePrinter.header("RANK LIST — SEMESTER " + semester, 55);
        List<Map.Entry<String, Double>> ranked =
                markSheetService.getRankList(semester, studentService);

        if (ranked.isEmpty()) {
            ConsolePrinter.empty("No results found for Semester " + semester + ".");
        } else {
            int rank = 1;
            for (Map.Entry<String, Double> entry : ranked) {
                Student s = studentService.findById(entry.getKey());
                System.out.printf("  #%-3d %-12s %-25s SGPA: %.2f%n",
                        rank++, s.getId(), s.getName(), entry.getValue());
            }
        }
        ConsolePrinter.divider(55);
        System.out.println();
    }

    public void printExamSchedule() {
        ConsolePrinter.header("EXAM SCHEDULE", 72);
        List<Exam> exams = examService.findAll();
        if (exams.isEmpty()) {
            ConsolePrinter.empty("No exams scheduled.");
        } else {
            for (Exam e : exams) System.out.println("  " + e);
        }
        ConsolePrinter.divider(72);
        System.out.println();
    }

    public void printExamResults(String examId) {
        Exam exam = examService.findById(examId);
        List<MarkSheet> sheets = markSheetService.getMarksByExam(examId);

        ConsolePrinter.header("EXAM RESULTS — " + exam.getSubject().getSubjectName()
                + " [" + exam.getExamType().getLabel() + "]", 72);

        if (!exam.isResultPublished()) {
            ConsolePrinter.warn("Results not yet published for this exam.");
        } else if (sheets.isEmpty()) {
            ConsolePrinter.empty("No marks entered.");
        } else {
            for (MarkSheet ms : sheets) {
                Student s = studentService.findById(ms.getStudentId());
                System.out.printf("  %-12s %-22s  %s%n",
                        s.getId(), s.getName(), ms);
            }
        }
        ConsolePrinter.divider(72);
        System.out.println();
    }
}
