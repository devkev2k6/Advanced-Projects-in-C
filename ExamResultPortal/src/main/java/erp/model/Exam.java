package erp.model;

import java.time.LocalDate;

public class Exam {

    private String    examId;
    private Subject   subject;
    private ExamType  examType;
    private LocalDate examDate;
    private String    examinerId;
    private boolean   resultPublished;

    public Exam(String examId, Subject subject, ExamType examType,
                LocalDate examDate, String examinerId) {
        this.examId          = examId;
        this.subject         = subject;
        this.examType        = examType;
        this.examDate        = examDate;
        this.examinerId      = examinerId;
        this.resultPublished = false;
    }

    public void publishResult()       { this.resultPublished = true; }
    public boolean isResultPublished() { return resultPublished; }

    public String    getExamId()     { return examId; }
    public Subject   getSubject()    { return subject; }
    public ExamType  getExamType()   { return examType; }
    public LocalDate getExamDate()   { return examDate; }
    public String    getExaminerId() { return examinerId; }

    @Override
    public String toString() {
        return String.format("[%s] %s | %s | %s | Date: %s | Published: %s",
                examId, subject.getSubjectName(), examType.getLabel(),
                subject.getDepartment(), examDate,
                resultPublished ? "Yes" : "No");
    }
}
