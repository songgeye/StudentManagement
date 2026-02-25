package raisetech.student.management.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.exception.TestException;
import raisetech.student.management.model.StudentDetailList;
import raisetech.student.management.service.StudentService;


@Tag(name = "受講生管理", description = "受講生の検索や登録、更新などを行うREST APIとして受け付けるControllerです。")
@Validated
@RestController
public class StudentController {

  private StudentService service;

  @Autowired
  public StudentController(StudentService service) {

    this.service = service;
  }

  @Operation(
      summary = "一覧検索",
      description = "受講生詳細の一覧検索です。 全件検索を行うので、条件指定は行いません",
      operationId = "getStudentsList"
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "受講生詳細一覧(全件)を返却します",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = StudentDetailList.class))),
      @ApiResponse(responseCode = "204", description = "受講生が存在しません",
          content = @Content)
  })
  @GetMapping("/studentsList")
  public List<StudentDetail> getStudentsList() {
    return service.searchStudentList();
  }

  @Operation(
      summary = "受講生詳細の単一検索",
      description = "受講生詳細の検索です。 IDに紐づく任意の受講生の情報を取得します。",
      operationId = "getStudent",
      parameters = {
          @Parameter(
              name = "id",
              description = "受講生情報の受講生ID",
              in = ParameterIn.PATH,
              required = true,
              example = "12345"
          )
      }
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "受講生情報(単一)を返却します",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = StudentDetail.class))),
      @ApiResponse(responseCode = "404", description = "IDが未定義です: {id}",
          content = @Content),
      @ApiResponse(responseCode = "400", description = "リクエストパラメータにエラーがあります: getStudent.id: 正規表現 \"^\\d+$\" にマッチさせてください",
          content = @Content)
  })
  @GetMapping("/student/{id}")
  public StudentDetail getStudent(
      @PathVariable Long id) {
    return service.searchStudent(id);
  }

  @Operation(
      summary = "受講生登録",
      description = "受講生詳細の登録を行います。",
      operationId = "registerStudent",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "登録する受講生の詳細情報",
          required = true,
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = StudentDetail.class),
              examples = {
                  @ExampleObject(
                      name = "基本的な受講生情報",
                      summary = "最小限の必須情報を含む例",
                      value = "{ \"student\": { \"name\": \"山田太郎\", \"kanaName\": \"ヤマダタロウ\", \"email\": \"yamada@example.com\", \"area\": \"東京\", \"age\": 25, \"gender\": \"男性\" }, \"studentsCourseList\": [{ \"courseName\": \"Spring Boot基礎\" }] }"
                  )
              }
          )
      )
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "受講生登録の実行結果を返却します",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = String.class))),
      @ApiResponse(responseCode = "400", description = "Bad Request - リクエストが不正です(エラー詳細は現在実装されていません)",
          content = @Content),
      @ApiResponse(responseCode = "500", description = "Internal Server Error(現在、詳細なエラーメッセージは実装されていません)",
          content = @Content)
  })
  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(
      @RequestBody @Valid StudentDetail studentDetail) {
    StudentDetail responseStudentDetail = service.registerStudent(studentDetail);
    return ResponseEntity.ok(responseStudentDetail);
  }

  @Operation(
      summary = "受講生更新",
      description = "受講生情報の更新をします。",
      operationId = "updateStudent",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "更新する受講生の詳細情報",
          required = true,
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = StudentDetail.class),
              examples = {
                  @ExampleObject(
                      name = "更新情報の例",
                      summary = "ID指定と更新したい情報を含む例",
                      value = "{ \"student\": { \"id\": 1, \"name\": \"山田太郎\", \"kanaName\": \"ヤマダタロウ\", \"email\": \"updated@example.com\", \"area\": \"大阪\", \"age\": 26, \"gender\": \"男性\", \"isDeleted\": false }, \"studentsCourseList\": [{ \"id\": 1, \"studentId\": 1, \"courseName\": \"Spring Boot応用\" }] }"
                  ),
                  @ExampleObject(
                      name = "論理削除の例",
                      summary = "受講生を論理削除する例",
                      value = "{ \"student\": { \"id\": 1, \"name\": \"山田太郎\", \"kanaName\": \"ヤマダタロウ\", \"email\": \"yamada@example.com\", \"area\": \"東京\", \"age\": 25, \"gender\": \"男性\", \"isDeleted\": true }, \"studentsCourseList\": [{ \"id\": 1, \"studentId\": 1, \"courseName\": \"Spring Boot基礎\" }] }"
                  )
              }
          )
      )
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "受講生更新の実行結果を返却します",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = String.class))),
      @ApiResponse(responseCode = "400", description = "Bad Request - リクエストが不正です(エラー詳細は現在実装されていません)",
          content = @Content),
      @ApiResponse(responseCode = "500", description = "Internal Server Error(現在、詳細なエラーメッセージは実装されていません)",
          content = @Content)
  })
  @PutMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(
      @RequestBody @Valid StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新処理が成功しました。");
  }

  @Operation(
      summary = "例外テスト",
      description = "TestExceptionをスローするテスト用エンドポイントです。エラーハンドリングの動作確認に使用します。",
      tags = "開発者ツール",
      parameters = {
          @Parameter(
              name = "debug",
              description = "デバッグ情報を含めるかどうか(オプション)",
              in = ParameterIn.QUERY,
              required = true,
              schema = @Schema(type = "boolean"),
              example = "true"
          )
      }
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "400", description = "テスト例外が発生しました(エラーレスポンスを返却します)",
          content = @Content(mediaType = "text/plain",
              schema = @Schema(implementation = String.class)))
  })
  @GetMapping("/test-exception")
  public String testException() throws TestException {
    throw new TestException("このAPIは現在利用できません。古いURLとなっています。");
  }
}
