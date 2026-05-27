package erp.util;

import erp.exception.InvalidDataException;

public class Validator {

    private Validator() {}

    public static void requireNonBlank(String value, String field) {
        if (value == null || value.trim().isEmpty())
            throw new InvalidDataException(field + " cannot be blank.");
    }

    public static void requireValidEmail(String email) {
        if (email == null || !email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$"))
            throw new InvalidDataException("Invalid email: " + email);
    }

    public static void requireRange(double val, double min, double max, String field) {
        if (val < min || val > max)
            throw new InvalidDataException(field + " must be between " + min + " and " + max + ". Got: " + val);
    }

    public static void requirePositive(int val, String field) {
        if (val <= 0)
            throw new InvalidDataException(field + " must be positive. Got: " + val);
    }
}
