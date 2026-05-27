package erp.service;

import erp.exception.DuplicateEntryException;
import erp.exception.InvalidDataException;
import erp.exception.ResultNotPublishedException;
import erp.model.*;
import erp.repository.MarkSheetRepository;
import erp.util.IDGenerator;
import erp.util.Validator;

import java.util.*;

public class MarkSheetService {

    private final MarkSheetRepository repo;
    private final ExamService         examService;
    private final StudentService      studentService;

    public MarkSheetService(MarkSheetRepository repo,
                             ExamService examService,
                             StudentService studentService) {
        this.repo           = repo;
        this.examService    = examService;
        this.studentService = studentService;
    }

    public MarkSheet enterMarks(String studentId, String examId, double marks) {
        studentService.findById(studentId);   // throws if student doesn't exist
        Exam exam = examService.findById(examId);

        if (repo.existsByStudentAndExam(studentId, examId))
            throw new DuplicateEntryException(
                    "Marks already entered for student " + studentId + " in exam " + examId);

        double total = exam.getExamType().getTotalMarks();
        if (marks != -1) Validator.requireRange(marks, 0, total, "Marks");   // -1 = absent

        MarkSheet ms = new MarkSheet(IDGenerator.nextMarkSheetId(), studentId, exam, marks);
        repo.save(ms);
        return ms;
    }

    // Students can only view results after the examiner publishes them
    public List<MarkSheet> getResultsForStudent(String studentId) {
        studentService.findById(studentId);
        List<MarkSheet> all = repo.findByStudentId(studentId);
        List<MarkSheet> visible = new ArrayList<>();
        for (MarkSheet ms : all)
            if (ms.getExam().isResultPublished()) visible.add(ms);
        return visible;
    }

    // Examiner view — all marks regardless of publish status
    public List<MarkSheet> getAllMarksForStudent(String studentId) {
        studentService.findById(studentId);
        return repo.findByStudentId(studentId);
    }

    public List<MarkSheet> getMarksByExam(String examId) {
        return repo.findByExamId(examId);
    }

    // Build a SemesterResult for a student across all their end-term published marksheets
    public SemesterResult getSemesterResult(String studentId, int semester) {
        List<MarkSheet> allMarks = repo.findByStudentId(studentId);
        SemesterResult sr = new SemesterResult(studentId, semester);

        for (MarkSheet ms : allMarks) {
            Subject subject = ms.getExam().getSubject();
            if (subject.getSemester() == semester && ms.getExam().isResultPublished()) {
                sr.addMarkSheet(ms);
            }
        }
        return sr;
    }

    // CGPA: average SGPA across all semesters (1–8) for a student
    public double calculateCGPA(String studentId, int maxSemester) {
        double total    = 0;
        int    counted  = 0;

        for (int sem = 1; sem <= maxSemester; sem++) {
            SemesterResult sr = getSemesterResult(studentId, sem);
            if (!sr.getMarkSheets().isEmpty()) {
                total += sr.calculateSGPA();
                counted++;
            }
        }
        return counted == 0 ? 0.0 : total / counted;
    }

    // Ranks all students in a semester by SGPA
    public List<Map.Entry<String, Double>> getRankList(int semester, StudentService studentService) {
        List<Student> students = studentService.findBySemester(semester);
        Map<String, Double> sgpaMap = new LinkedHashMap<>();

        for (Student s : students) {
            SemesterResult sr = getSemesterResult(s.getId(), semester);
            sgpaMap.put(s.getId(), sr.calculateSGPA());
        }

        List<Map.Entry<String, Double>> ranked = new ArrayList<>(sgpaMap.entrySet());
        ranked.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));
        return ranked;
    }
}
