package raisetech.student.management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "受講生")
@Getter
@Setter
@NoArgsConstructor
public class Student {

  @Pattern(regexp = "^\\d+$")
  private String id;

  private String name;
  private String kanaName;
  private String nickname;

  @Email
  private String email;

  private String area;
  private int age;
  private String gender;
  private String remark;
  private boolean isDeleted;
}
