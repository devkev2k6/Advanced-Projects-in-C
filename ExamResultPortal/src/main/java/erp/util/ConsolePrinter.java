package erp.util;

public class ConsolePrinter {

    private ConsolePrinter() {}

    public static void divider(int width)  { System.out.println("─".repeat(width)); }
    public static void doubleLine(int w)   { System.out.println("═".repeat(w)); }

    public static void header(String title, int width) {
        doubleLine(width);
        System.out.printf("  %-" + (width - 4) + "s%n", title);
        doubleLine(width);
    }

    public static void sectionHeader(String title) {
        System.out.println();
        System.out.println("  ── " + title + " " + "─".repeat(Math.max(0, 48 - title.length())));
    }

    public static void info(String label, String value) {
        System.out.printf("  %-18s: %s%n", label, value);
    }

    public static void success(String msg) { System.out.println("  ✔  " + msg); }
    public static void error(String msg)   { System.out.println("  ✘  " + msg); }
    public static void warn(String msg)    { System.out.println("  ⚠  " + msg); }
    public static void empty(String msg)   { System.out.println("  >> " + msg); }
}
