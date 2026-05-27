package erp;

import erp.exception.DuplicateEntryException;
import erp.exception.NotFoundException;
import erp.model.*;
import erp.repository.*;
import erp.service.*;
import org.junit.jupiter.api.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MarkSheetServiceTest {

    private static StudentService   studentService;
    private static ExaminerService  examinerService;
    private static SubjectService   subjectService;
    private static ExamService      examService;
    private static MarkSheetService markSheetService;

    private static String studentId;
    private static String examId;

    @BeforeAll
    static void setup() {
        studentService   = new StudentService(new StudentRepository());
        examinerService  = new ExaminerService(new ExaminerRepository());
        subjectService   = new SubjectService(new SubjectRepository());
        examService      = new ExamService(new ExamRepository(), subjectService, examinerService);
        markSheetService = new MarkSheetService(new MarkSheetRepository(), examService, studentService);

        Student  s = studentService.register("Priya Sharma", "priya@college.in",
                                              "CSE", 3, "2022-2026", "CSE/22/101");
        Examiner e = examinerService.add("Prof. Kumar", "kumar@college.in", "CSE", "Professor");
        Subject  sub = subjectService.add("CS", "Algorithms", 4, 3, "CSE");
        Exam     exam = examService.schedule(sub.getSubjectCode(),
                                              ExamType.END_TERM, LocalDate.now(), e.getId());
        studentId = s.getId();
        examId    = exam.getExamId();
    }

    @Test @Order(1)
    void testEnterMarks() {
        MarkSheet ms = markSheetService.enterMarks(studentId, examId, 62);
        assertNotNull(ms);
        assertEquals(ResultStatus.PASS, ms.getStatus());
        assertEquals(Grade.B_PLUS, ms.getGrade());
    }

    @Test @Order(2)
    void testDuplicateMarksThrows() {
        assertThrows(DuplicateEntryException.class,
                () -> markSheetService.enterMarks(studentId, examId, 55));
    }

    @Test @Order(3)
    void testResultNotVisibleBeforePublish() {
        assertTrue(markSheetService.getResultsForStudent(studentId).isEmpty());
    }

    @Test @Order(4)
    void testResultVisibleAfterPublish() {
        examService.publishResult(examId);
        assertFalse(markSheetService.getResultsForStudent(studentId).isEmpty());
    }

    @Test @Order(5)
    void testAbsentEntry() {
        Subject  sub2 = subjectService.add("CS", "OS", 3, 3, "CSE");
        Examiner e2   = examinerService.add("Prof. Das", "das@college.in", "CSE", "Asst. Prof");
        Exam     ex2  = examService.schedule(sub2.getSubjectCode(),
                                              ExamType.MID_TERM, LocalDate.now(), e2.getId());
        MarkSheet ms = markSheetService.enterMarks(studentId, ex2.getExamId(), -1);
        assertEquals(ResultStatus.ABSENT, ms.getStatus());
    }

    @Test @Order(6)
    void testInvalidStudentThrows() {
        assertThrows(NotFoundException.class,
                () -> markSheetService.enterMarks("STU9999", examId, 50));
    }

    @Test @Order(7)
    void testGradeFromScore() {
        assertEquals(Grade.O,      Grade.fromPercentage(95));
        assertEquals(Grade.A_PLUS, Grade.fromPercentage(82));
        assertEquals(Grade.F,      Grade.fromPercentage(30));
    }

    @Test @Order(8)
    void testSGPACalculation() {
        SemesterResult sr = markSheetService.getSemesterResult(studentId, 3);
        assertTrue(sr.calculateSGPA() > 0);
    }
}
