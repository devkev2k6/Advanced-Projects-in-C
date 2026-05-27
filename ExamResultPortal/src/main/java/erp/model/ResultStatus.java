package erp.model;

public enum ResultStatus {
    PASS,
    FAIL,
    ABSENT,
    WITHHELD;   // result held back due to dues / disciplinary

    // A student passes if they score at least 40% of total marks
    public static ResultStatus evaluate(double scored, double total) {
        if (scored < 0)          return ABSENT;
        return (scored / total) >= 0.40 ? PASS : FAIL;
    }
}
