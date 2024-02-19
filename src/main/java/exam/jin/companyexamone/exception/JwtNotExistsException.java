package exam.jin.companyexamone.exception;

public class JwtNotExistsException extends RuntimeException {
    public JwtNotExistsException(String message) {
        super(message);
    }
}
