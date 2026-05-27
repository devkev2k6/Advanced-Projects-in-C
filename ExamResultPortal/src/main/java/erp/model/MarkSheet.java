package erp.model;

public class MarkSheet {

    private String       markSheetId;
    private String       studentId;
    private Exam         exam;
    private double       marksObtained;   // -1 means absent
    private Grade        grade;
    private ResultStatus status;

    public MarkSheet(String markSheetId, String studentId, Exam exam, double marksObtained) {
        this.markSheetId    = markSheetId;
        this.studentId      = studentId;
        this.exam           = exam;
        this.marksObtained  = marksObtained;

        double total = exam.getExamType().getTotalMarks();
        this.status  = ResultStatus.evaluate(marksObtained, total);

        double pct   = marksObtained < 0 ? 0 : (marksObtained / total) * 100;
        this.grade   = (status == ResultStatus.ABSENT) ? Grade.F : Grade.fromPercentage(pct);
    }

    public String       getMarkSheetId()  { return markSheetId; }
    public String       getStudentId()    { return studentId; }
    public Exam         getExam()         { return exam; }
    public double       getMarksObtained(){ return marksObtained; }
    public Grade        getGrade()        { return grade; }
    public ResultStatus getStatus()       { return status; }

    public double getPercentage() {
        if (marksObtained < 0) return 0;
        return (marksObtained / exam.getExamType().getTotalMarks()) * 100;
    }

    @Override
    public String toString() {
        String marks = marksObtained < 0 ? "ABSENT"
                : String.format("%.1f / %d", marksObtained, exam.getExamType().getTotalMarks());
        return String.format("%-35s | %-12s | Marks: %-12s | Grade: %-4s | %s",
                exam.getSubject().getSubjectName(),
                exam.getExamType().getLabel(),
                marks,
                grade.getLabel(),
                status);
    }
}
