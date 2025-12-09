package raisetech.student.management.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));

    verify(service, times(1)).searchStudentList();
  }

  @Test
  void 受講生詳細の検索ができて空で返ってくること() throws Exception {
    String id = "999";
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
                             "age" : "31",
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
  void 受講生更新で適切なデータを送信した時に正常に更新できること() throws Exception {
    // 1. 更新用のStudentオブジェクトを作成
    //    - 既存のIDを設定（例："1"）
    //    - 更新したい値を設定（name, email, age等）
    Student student = new Student();
    List<StudentCourse> studentsCourseList = new ArrayList<>();
    StudentDetail requestBody = new StudentDetail(student, studentsCourseList);

    student.setId("999");
    student.setName("松ヶ野健吾");
    student.setKanaName("マツガノケンゴ");
    student.setNickname("そん");
    student.setEmail("test@example.com");
    student.setArea("神奈川県");
    student.setAge(31);
    student.setGender("M");
    student.setRemark("テスト更新です");
    student.setDeleted(false);

    // 2. StudentCourseリストを作成
    //    - 更新対象のコース情報を設定
    //    - 既存のコースIDも設定
    StudentCourse studentCourse = new StudentCourse();

    studentCourse.setId("999");
    studentCourse.setStudentId("999");
    studentCourse.setCourseName("WordPressコース");
    studentCourse.setCourseStartAt(LocalDateTime.parse("2025-12-07T00:00:00"));
    studentCourse.setCourseEndAt(LocalDateTime.parse("2026-12-07T00:00:00"));
    studentsCourseList.add(studentCourse);

    // 3. 更新用のStudentDetailを作成
    //    - StudentとStudentCourseリストを組み合わせ
//    StudentDetail studentDetail = new StudentDetail(student, studentsCourseList);

    // 4. Serviceのモック設定
    //    - updateStudentメソッドは戻り値がvoid
    //    - doNothing().when(service).updateStudent(any(StudentDetail.class))
    //    または when().thenReturn()は不要
    doNothing().when(service).updateStudent(any(StudentDetail.class));

    // 5. ObjectMapperでJavaオブジェクト→JSON文字列に変換
    //    - JavaTimeModuleも忘れずに設定
    ObjectMapper objectMapper = new ObjectMapper()
        .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
    String requestJson = objectMapper.writeValueAsString(requestBody);

    // 6. MockMvcでPUTリクエスト実行
    //    - put("/updateStudent")  ← PUTメソッド使用
    //    - contentType(APPLICATION_JSON)
    //    - content(JSON文字列)
    mockMvc.perform(put("/updateStudent").contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))

        // 7. レスポンス検証
        //    - status().isOk()
        //    - jsonPath()または単純なテキスト検証
        //    - 更新成功メッセージの確認
        .andExpect(status().isOk());

    // 8. Serviceメソッド呼び出し確認
    //    - verify(service, times(1)).updateStudent(any(StudentDetail.class))
    verify(service, times(1)).updateStudent(any(StudentDetail.class));
  }

  @Test
  void 受講生更新で不正なデータを送信した時に400エラーが返ること() throws Exception {
    // 1. 直接不正なJSONデータを作成
    //    - POST異常系と同じアプローチ
    //    - "{\"student\":{\"email\":\"不正なメール形式\"}}"
    //    - または他のバリデーション違反データ

    // 2. Serviceのモック設定は不要
    //    - バリデーションで弾かれるため、Serviceが呼ばれない可能性
    //    - または doNothing().when()で設定

    // 3. MockMvcでPUTリクエスト実行
    //    - put("/updateStudent")
    //    - contentType(APPLICATION_JSON)
    //    - content(不正なJSON文字列)
    mockMvc.perform(put("/updateStudent").contentType(MediaType.APPLICATION_JSON)
            .content("{\"student\":{\"email\":\"不正なメール\"}}"))

        // 4. レスポンス検証
        //    - status().isBadRequest() ← 400エラーを期待
        //    - JSONパス検証は不要（エラーレスポンス形式による）
        .andExpect(status().isBadRequest());

    // 5. Serviceメソッド呼び出し確認
    //    - verify(service, never()).updateStudent(any())
    //    - または verify自体をスキップ
  }
}