package erp.model;

public class Student extends User {

    private int    semester;
    private String batch;      // e.g. "2022-2026"
    private String rollNumber;

    public Student(String id, String name, String email, String department,
                   int semester, String batch, String rollNumber) {
        super(id, name, email, department);
        this.semester   = semester;
        this.batch      = batch;
        this.rollNumber = rollNumber;
    }

    @Override
    public String getRole() { return "STUDENT"; }

    public int    getSemester()   { return semester; }
    public String getBatch()      { return batch; }
    public String getRollNumber() { return rollNumber; }

    public void setSemester(int semester) { this.semester = semester; }

    @Override
    public String toString() {
        return super.toString() +
               String.format(" | Roll: %s | Batch: %s | Sem: %d", rollNumber, batch, semester);
    }
}
