package erp.model;

public enum ExamType {
    MID_TERM("Mid-Term",   30),
    END_TERM("End-Term",   70),
    PRACTICAL("Practical", 50),
    VIVA("Viva",           20),
    ASSIGNMENT("Assignment", 10);

    private final String label;
    private final int    totalMarks;

    ExamType(String label, int totalMarks) {
        this.label      = label;
        this.totalMarks = totalMarks;
    }

    public String getLabel()      { return label; }
    public int    getTotalMarks() { return totalMarks; }

    @Override
    public String toString() { return label + " (/" + totalMarks + ")"; }
}
