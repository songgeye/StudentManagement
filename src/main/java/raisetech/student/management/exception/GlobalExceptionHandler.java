package raisetech.student.management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(TestException.class)
  public ResponseEntity<String> handleTestException(TestException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

  @ExceptionHandler(BaseException.class)
  public ResponseEntity<String> handleBaseException(BaseException ex) {
    return new ResponseEntity<>("現在このAPIは利用できません", HttpStatus.SERVICE_UNAVAILABLE);
  }
}
