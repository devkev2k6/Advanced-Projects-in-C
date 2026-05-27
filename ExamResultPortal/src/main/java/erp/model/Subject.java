package erp.model;

public class Subject {

    private String subjectCode;
    private String subjectName;
    private int    credits;
    private int    semester;
    private String department;

    public Subject(String subjectCode, String subjectName,
                   int credits, int semester, String department) {
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.credits     = credits;
        this.semester    = semester;
        this.department  = department;
    }

    public String getSubjectCode() { return subjectCode; }
    public String getSubjectName() { return subjectName; }
    public int    getCredits()     { return credits; }
    public int    getSemester()    { return semester; }
    public String getDepartment()  { return department; }

    @Override
    public String toString() {
        return String.format("%s | %-35s | Credits: %d | Sem: %d | %s",
                subjectCode, subjectName, credits, semester, department);
    }
}
