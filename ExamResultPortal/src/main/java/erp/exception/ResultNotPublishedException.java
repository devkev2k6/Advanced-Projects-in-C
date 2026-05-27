package erp.exception;

public class ResultNotPublishedException extends RuntimeException {
    public ResultNotPublishedException(String examId) {
        super("Result for exam '" + examId + "' has not been published yet.");
    }
}
