package raisetech.student.management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "受講生")
@Getter
@Setter
@NoArgsConstructor
public class Student {

  private Long id;

  @NotBlank
  private String name;

  @NotBlank
  private String kanaName;

  private String nickname;

  @NotBlank
  @Email
  private String email;

  @NotBlank
  private String area;

  private int age;

  @NotBlank
  private String gender;

  private String remark;
  private boolean isDeleted;
}
