package erp.model;

public enum Grade {
    O ("O",  10.0, "Outstanding"),
    A_PLUS("A+", 9.0,  "Excellent"),
    A ("A",  8.0,  "Very Good"),
    B_PLUS("B+", 7.0,  "Good"),
    B ("B",  6.0,  "Above Average"),
    C ("C",  5.0,  "Average"),
    P ("P",  4.0,  "Pass"),
    F ("F",  0.0,  "Fail");

    private final String label;
    private final double gradePoint;
    private final String description;

    Grade(String label, double gradePoint, String description) {
        this.label       = label;
        this.gradePoint  = gradePoint;
        this.description = description;
    }

    public String getLabel()       { return label; }
    public double getGradePoint()  { return gradePoint; }
    public String getDescription() { return description; }

    // Converts a percentage (0–100) to a Grade
    public static Grade fromPercentage(double pct) {
        if (pct >= 90) return O;
        if (pct >= 80) return A_PLUS;
        if (pct >= 70) return A;
        if (pct >= 60) return B_PLUS;
        if (pct >= 50) return B;
        if (pct >= 45) return C;
        if (pct >= 40) return P;
        return F;
    }

    @Override
    public String toString() { return label + " (" + description + ")"; }
}
