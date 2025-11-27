package raisetech.student.management.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "受講生コース情報")
@Getter
@Setter
@NoArgsConstructor
public class StudentCourse {

  @Pattern(regexp = "^\\d+$")
  private String id;

  @Pattern(regexp = "^\\d+$")
  private String studentId;

  private String courseName;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime courseStartAt;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime courseEndAt;
}
