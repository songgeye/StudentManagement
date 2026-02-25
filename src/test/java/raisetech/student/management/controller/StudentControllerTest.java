package raisetech.student.management.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import raisetech.student.management.data.Student;
import raisetech.student.management.service.StudentService;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @MockitoBean
  private StudentService service;

  @Test
  void 受講生詳細の一覧検索が実行できて空のリストが返ってくること() throws Exception {
    mockMvc.perform(get("/studentsList"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));

    verify(service, times(1)).searchStudentList();
  }

  @Test
  void 受講生詳細の検索ができて空で返ってくること() throws Exception {
    Long id = 999L;
    mockMvc.perform(get("/student/{id}", id))
        .andExpect(status().isOk());

    verify(service, times(1)).searchStudent(id);
  }

  @Test
  void 受講生詳細の登録が実行できてからで返ってくること() throws Exception {
    // リクエストデータは適切に構築して入力チェックの検証も兼ねている。
    // 本来であれば返りは登録されたデータが入るが、モック化されると意味がないため、レスポンスは作らない。
    mockMvc.perform(post("/registerStudent").contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                       {
                           "student" : {
                             "name" : "松ヶ野健吾",
                             "kanaName" : "マツガノケンゴ",
                             "nickname" : "son",
                             "email" : "test@example.com",
                             "area" : "神奈川県",
                             "age" : 31,
                             "gender" : "M",
                             "remark" : ""
                          },
                          "studentCourseList" : [
                            {
                                "courseName" : "Javaコース"
                            }
                          ]
                      }
                    """
            ))
        .andExpect(status().isOk());

    verify(service, times(1)).registerStudent(any());
  }

  @Test
  void 受講生詳細の更新が実行できて空で返ってくること() throws Exception {
    // リクエストデータは適切に構築して入力チェックの検証も兼ねている。
    mockMvc.perform(put("/updateStudent").contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                     {
                         "student" : {
                           "id" : 12,
                           "name" : "松ヶ野健吾",
                           "kanaName" : "マツガノケンゴ",
                           "nickname" : "son",
                           "email" : "test@example.com",
                           "area" : "神奈川県",
                           "age" : 31,
                           "gender" : "M",
                           "remark" : ""
                        },
                        "studentCourseList" : [
                          {
                              "id" : "15",
                              "studentId" : "12",
                              "courseName" : "Javaコース",
                              "courseStartAt" : "2025-11-27T00:00:00",
                              "courseEndAt" : "2026-11-27T00:00:00"
                          }
                        ]
                    }
                    """
            ))
        .andExpect(status().isOk());

    verify(service, times(1)).updateStudent(any());
  }

  @Test
  void 受講生詳細の例外APIが実行できてステータスが400で返ってくること() throws Exception {
    mockMvc.perform(get("/test-exception"))
        .andExpect(status().is4xxClientError())
        .andExpect(content().string("このAPIは現在利用できません。古いURLとなっています。"));
  }

  @Test
  void 受講生詳細の受講生で適切な値を入力した時に入力チェックに異常が発生しないこと() {
    Student student = new Student();
    student.setId(1L);
    student.setName("松ヶ野健吾");
    student.setKanaName("マツガノケンゴ");
    student.setNickname("そん");
    student.setEmail("test@example.com");
    student.setArea("神奈川県");
    student.setGender("M");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(0);
  }

  @Test
  void 受講生詳細の受講生でIDが0の場合に入力チェックが掛かること() {
    Student student = new Student();
    student.setId(0L);
    student.setName("松ヶ野健吾");
    student.setKanaName("マツガノケンゴ");
    student.setNickname("そん");
    student.setEmail("test@example.com");
    student.setArea("神奈川県");
    student.setGender("M");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations).extracting("message")
        .containsOnly("IDは1以上である必要があります");
  }
}