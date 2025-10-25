package raisetech.student.management.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(TestException.class)
  public ResponseEntity<String> handleTestException(TestException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<String> handleNotFoundException(NotFoundException ex,
      HttpServletRequest request) {
    logger.warn("404 NotFound: {} {} - {}", request.getMethod(), request.getRequestURI(),
        ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  /**
   * RequestBody バリデーションエラー用のハンドラー
   *
   * @param ex
   * @return
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<String> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
    // エラーメッセージを収集
    List<String> errors = ex.getBindingResult().getFieldErrors().stream()
        .map(error -> error.getField() + ": " + error.getDefaultMessage())
        .collect(Collectors.toList());

    // シンプルなメッセージ形式
    String errorMessage = "入力内容にエラーがあります: " + String.join(", ", errors);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
  }

  /**
   * PathVariable などのバリデーションエラー用のハンドラー
   *
   * @param ex
   * @return
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<String> handleConstraintViolation(ConstraintViolationException ex) {
    // エラーメッセージを収集
    List<String> errors = new ArrayList<>();
    Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();

    violations.forEach(violation -> {
      String propertyPath = violation.getPropertyPath().toString();
      String message = violation.getMessage();
      errors.add(propertyPath + ": " + message);
    });

    // シンプルなメッセージ形式
    String errorMessage = "リクエストパラメータにエラーがあります: " + String.join(", ", errors);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
  }
}
