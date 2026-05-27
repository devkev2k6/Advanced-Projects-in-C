package erp.util;

public class IDGenerator {

    private static int studentCounter  = 1000;
    private static int examinerCounter = 100;
    private static int examCounter     = 1;
    private static int markSheetCounter= 1;
    private static int subjectCounter  = 1;

    private IDGenerator() {}

    public static String nextStudentId()   { return "STU" + (++studentCounter); }
    public static String nextExaminerId()  { return "EXM" + (++examinerCounter); }
    public static String nextExamId()      { return "EXAM" + String.format("%04d", examCounter++); }
    public static String nextMarkSheetId() { return "MS"   + String.format("%05d", markSheetCounter++); }
    public static String nextSubjectCode(String prefix) {
        return prefix.toUpperCase() + String.format("%03d", subjectCounter++);
    }
}
