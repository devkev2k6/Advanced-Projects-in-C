package erp.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SemesterResult {

    private String         studentId;
    private int            semester;
    private List<MarkSheet> markSheets;

    public SemesterResult(String studentId, int semester) {
        this.studentId  = studentId;
        this.semester   = semester;
        this.markSheets = new ArrayList<>();
    }

    public void addMarkSheet(MarkSheet ms) {
        markSheets.add(ms);
    }

    public List<MarkSheet> getMarkSheets() {
        return Collections.unmodifiableList(markSheets);
    }

    // SGPA: credit-weighted grade points for this semester's end-term marks
    public double calculateSGPA() {
        double totalPoints  = 0;
        int    totalCredits = 0;

        for (MarkSheet ms : markSheets) {
            if (ms.getExam().getExamType() == ExamType.END_TERM) {
                int credits   = ms.getExam().getSubject().getCredits();
                totalPoints  += ms.getGrade().getGradePoint() * credits;
                totalCredits += credits;
            }
        }
        return totalCredits == 0 ? 0.0 : totalPoints / totalCredits;
    }

    // True only if the student passed all subjects in this semester
    public boolean isOverallPass() {
        for (MarkSheet ms : markSheets) {
            if (ms.getStatus() == ResultStatus.FAIL) return false;
        }
        return !markSheets.isEmpty();
    }

    public int    getTotalSubjects() { return markSheets.size(); }
    public String getStudentId()     { return studentId; }
    public int    getSemester()      { return semester; }
}
