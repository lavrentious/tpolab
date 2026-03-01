package lab1.task3.domain.exception;

public class ChainOfCommandViolationException extends RuntimeException {
  public ChainOfCommandViolationException(String message) {
    super(message);
  }
}