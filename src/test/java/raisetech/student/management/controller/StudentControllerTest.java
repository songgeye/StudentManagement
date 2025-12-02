package raisetech.student.management.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
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
        .andExpect(status().isOk());

    verify(service, times(1)).searchStudentList();
  }

  @Test
  void 受講生詳細の受講生で適切な値を入力した時に入力チェックに異常が発生しないこと() {
    Student student = new Student();
    student.setId("1");
    student.setName("松ヶ野健吾");
    student.setKanaName("マツガノケンゴ");
    student.setNickname("そん");
    student.setEmail("test@example.com");
    student.setArea("神奈川県");
    student.setAge(31);
    student.setGender("M");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(0);
  }

  @Test
  void 受講生詳細の受講生でIDに数字以外を用いたときに入力チェックに掛かること() {
    Student student = new Student();
    student.setId("テストです");
    student.setName("松ヶ野健吾");
    student.setKanaName("マツガノケンゴ");
    student.setNickname("そん");
    student.setEmail("test@example.com");
    student.setArea("神奈川県");
    student.setAge(31);
    student.setGender("M");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations).extracting("message")
        .containsOnly("数値のみ入力するようにしてください。");
  }

  @Test
  void 適切なIDで受講生情報が取得できること() throws Exception {
    String testId = "999";
    Student student = new Student();
    student.setId(testId);
    StudentDetail expected = new StudentDetail(student, new ArrayList<>());

    when(service.searchStudent(testId)).thenReturn(expected);

    mockMvc.perform(get("/student/{id}", testId).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.student.id").value("999"));

    verify(service, times(1)).searchStudent(testId);
    assertThat(expected.getStudent().getId()).isEqualTo(testId);
  }

  @Test
  void 不正なIDで受講生情報を取得しようとした時に400エラーが返ること() throws Exception {
    String testId = "テストです";
    Student student = new Student();
    student.setId(testId);
    StudentDetail expected = new StudentDetail(student, new ArrayList<>());

    when(service.searchStudent(testId)).thenReturn(expected);

    mockMvc.perform(get("/student/{id}", testId).contentType(MediaType.APPLICATION_JSON)
            .content(testId))
        .andExpect(status().isBadRequest());
  }

  @Test
  void 受講生登録で適切なデータを送信した時に正常に登録できること() throws Exception {
    Student student = new Student();
    List<StudentCourse> studentsCourseList = new ArrayList<>();
    StudentDetail requestBody = new StudentDetail(student, studentsCourseList);

    // 1. Studentオブジェクトに具体的な値を設定（name, email, age等）
    student.setId("999");
    student.setName("松ヶ野健吾");
    student.setKanaName("マツガノケンゴ");
    student.setNickname("そん");
    student.setEmail("test@example.com");
    student.setArea("神奈川県");
    student.setAge(31);
    student.setGender("M");
    student.setRemark("テストです");
    student.setDeleted(false);

    // 2. StudentCourseオブジェクトを作成してリストに追加
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setId("999");
    studentCourse.setStudentId("999");
    studentCourse.setCourseName("Javaコース");
    studentCourse.setCourseStartAt(LocalDateTime.parse("2025-11-27T00:00:00"));
    studentCourse.setCourseEndAt(LocalDateTime.parse("2026-11-27T00:00:00"));
    studentsCourseList.add(studentCourse);

    // 3. Serviceのモック設定（when().thenReturn()）
    when(service.registerStudent(any(StudentDetail.class))).thenReturn(requestBody);

    // 4. ObjectMapperでJavaオブジェクト→JSON文字列に変換
    ObjectMapper objectMapper = new ObjectMapper()
        .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
    String requestJson = objectMapper.writeValueAsString(requestBody);

    // 5. MockMvcでPOSTリクエスト実行
    //    - post("/registerStudent")
    //    - contentType(APPLICATION_JSON)
    //    - content(JSON文字列)
    mockMvc.perform(post("/registerStudent").contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
        // 6. レスポンス検証
        //    - status().isOk() または status().isCreated()
        //    - jsonPath()でレスポンス内容確認
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.student.id").value("999"))
        .andExpect(jsonPath("$.student.name").value("松ヶ野健吾"))
        .andExpect(jsonPath("$.student.kanaName").value("マツガノケンゴ"))
        .andExpect(jsonPath("$.student.nickname").value("そん"))
        .andExpect(jsonPath("$.student.email").value("test@example.com"))
        .andExpect(jsonPath("$.student.area").value("神奈川県"))
        .andExpect(jsonPath("$.student.age").value(31))
        .andExpect(jsonPath("$.student.gender").value("M"))
        .andExpect(jsonPath("$.student.remark").value("テストです"))
        .andExpect(jsonPath("$.student.deleted").value(false))
        .andExpect(jsonPath("$.studentsCourseList[0].id").value("999"))
        .andExpect(jsonPath("$.studentsCourseList[0].studentId").value("999"))
        .andExpect(jsonPath("$.studentsCourseList[0].courseName").value("Javaコース"))
        .andExpect(jsonPath("$.studentsCourseList[0].courseStartAt").value("2025-11-27T00:00:00"))
        .andExpect(jsonPath("$.studentsCourseList[0].courseEndAt").value("2026-11-27T00:00:00"));

    // 7. Serviceメソッド呼び出し確認（verify）
    verify(service, times(1)).registerStudent(any(StudentDetail.class));
  }

  @Test
  void 受講生登録で不正なデータを送信した時に400エラーが返ること() throws Exception {
    //  MockMvcでPOSTリクエスト実行
    //    - post("/registerStudent")
    //    - contentType(APPLICATION_JSON)
    //    - content(不正なJSON文字列)
    mockMvc.perform(post("/registerStudent").contentType(MediaType.APPLICATION_JSON)
            .content("{\"student\":{\"email\":\"不正なメール\"}}"))
        //  レスポンス検証
        //    - status().isBadRequest() ← 400エラーを期待
        //    - （オプション）jsonPath()でエラーメッセージ確認
        .andExpect(status().isBadRequest());

    //  Serviceメソッド呼び出し確認
    //    - verify(service, never()).registerStudent(any()) ← 呼ばれないことを確認
    //    または verify の確認自体をスキップ
  }

  @Test
  void 受講生更新で適切なデータを送信した時に正常に更新できること() {
    // 1. 更新用のStudentオブジェクトを作成
    //    - 既存のIDを設定（例："1"）
    //    - 更新したい値を設定（name, email, age等）

    // 2. StudentCourseリストを作成
    //    - 更新対象のコース情報を設定
    //    - 既存のコースIDも設定

    // 3. 更新用のStudentDetailを作成
    //    - StudentとStudentCourseリストを組み合わせ

    // 4. Serviceのモック設定
    //    - updateStudentメソッドは戻り値がvoid
    //    - doNothing().when(service).updateStudent(any(StudentDetail.class))
    //    または when().thenReturn()は不要

    // 5. ObjectMapperでJavaオブジェクト→JSON文字列に変換
    //    - JavaTimeModuleも忘れずに設定

    // 6. MockMvcでPUTリクエスト実行
    //    - put("/updateStudent")  ← PUTメソッド使用
    //    - contentType(APPLICATION_JSON)
    //    - content(JSON文字列)

    // 7. レスポンス検証
    //    - status().isOk()
    //    - jsonPath()または単純なテキスト検証
    //    - 更新成功メッセージの確認

    // 8. Serviceメソッド呼び出し確認
    //    - verify(service, times(1)).updateStudent(any(StudentDetail.class))
  }
}