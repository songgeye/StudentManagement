package raisetech.student.management.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StudentCourse {

  @Pattern(regexp = "^\\d+$")
  private String id;

  @Pattern(regexp = "^\\d+$")
  private String studentId;

  private String courseName;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDateTime courseStartAt;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDateTime courseEndAt;
}
