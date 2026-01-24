package raisetech.student.management.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  @Test
  void 受講生の全件検索が行えること() {
    List<Student> actual = sut.search();
    assertThat(actual.size()).isEqualTo(5);
  }

  @Test
  @Sql("classpath:truncate-student.sql")
  void 受講生情報が存在しない場合に空のリストが返ること() {
    List<Student> actual = sut.search();
    assertThat(actual).isEmpty();

    assertThat(actual.size()).isEqualTo(0);
  }

  @Test
  void 受講生の検索が行えること() {
    Long id = 1L;
    Student actual = sut.searchStudent(id);
    assertThat(actual.getId()).isEqualTo(1L);
  }

  @Test
  void 受講生のIDに存在しないIDで検索した時に入力チェックが掛かること() {
    Long id = 999L;
    Student actual = sut.searchStudent(id);
    assertThat(actual).isNull();
  }

  @Test
  void 受講生のコース情報の全件検索を行えること() {
    List<StudentCourse> actual = sut.searchStudentCourseList();
    assertThat(actual.size()).isEqualTo(8);
  }

  @Test
  @Sql("classpath:truncate-student-courses.sql")
  void 受講生コース情報が存在しない場合に空のリストが返ること() {

    List<StudentCourse> actual = sut.searchStudentCourseList();
    assertThat(actual).isEmpty();

    assertThat(actual.size()).isEqualTo(0);
  }

  @Test
  void 受講生IDに紐づく受講生コース情報の検索が行えること() {
    Long studentId = 1L;
    List<StudentCourse> actual = sut.searchStudentCourse(studentId);
    assertThat(actual.get(1).getId()).isEqualTo(2L);
  }

  @Test
  void 存在しない受講生IDの場合はからのリストが返ること() {
    Long studentId = 999L;
    List<StudentCourse> actual = sut.searchStudentCourse(studentId);
    assertThat(actual).isEmpty();
  }

  @Test
  void 受講生の登録が行えること() {
    Student student = new Student();
    student.setName("松ヶ野健吾");
    student.setKanaName("マツガノケンゴ");
    student.setNickname("そん");
    student.setEmail("test@example.com");
    student.setArea("神奈川県");
    student.setGender("M");
    student.setRemark("");
    student.setDeleted(false);

    sut.registerStudent(student);

    List<Student> actual = sut.search();

    assertThat(actual.size()).isEqualTo(6);
  }

  @Test
  void 受講生の必須項目が不足している場合に例外が発生すること() {
    Student student = new Student();
    student.setKanaName("マツガノケンゴ");

    assertThatThrownBy(() -> sut.registerStudent(student))
        .isInstanceOf(DataIntegrityViolationException.class);
  }

  @Test
  void 受講生コースの登録が行えること() {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setStudentId(1L);
    studentCourse.setCourseName("Javaコース");
    studentCourse.setCourseStartAt(LocalDateTime.now());
    studentCourse.setCourseEndAt(LocalDateTime.now().plusYears(1));

    sut.registerStudentCourse(studentCourse);

    List<StudentCourse> actual = sut.searchStudentCourseList();

    assertThat(actual.size()).isEqualTo(9);
  }

  @Test
  void 受講生コースIDがない場合に登録ができないこと() {
    StudentCourse studentCourse = new StudentCourse();

    assertThatThrownBy(() -> sut.registerStudentCourse(studentCourse))
        .isInstanceOf(DataIntegrityViolationException.class);
  }

  @Test
  void 受講生の更新が行えること() {
    Student student = new Student();
    student.setName("松ヶ野健吾");
    student.setKanaName("マツガノケンゴ");
    student.setNickname("そん");
    student.setEmail("test2@example.com");
    student.setArea("東京都");
    student.setGender("M");
    student.setRemark("");
    student.setDeleted(false);

    sut.updateStudent(student);

    List<Student> actual = sut.search();

    assertThat(actual.size()).isEqualTo(5);
  }

  @Test
  void 存在しない受講生IDで更新しようとした場合に更新されないこと() {
    Student student = new Student();
    int beforeCount = sut.search().size();
    sut.updateStudent(student);
    int afterCount = sut.search().size();
    assertThat(afterCount).isEqualTo(beforeCount);
  }

  @Test
  void 受講生コースの更新が行えること() {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setStudentId(2L);
    studentCourse.setCourseName("AWSコース");
    studentCourse.setCourseStartAt(LocalDateTime.now());
    studentCourse.setCourseEndAt(LocalDateTime.now().plusYears(1));

    sut.updateStudentCourse(studentCourse);

    List<StudentCourse> actual = sut.searchStudentCourseList();

    assertThat(actual.size()).isEqualTo(8);
  }

  @Test
  void 受講生コースIDがない場合に更新できないこと() {
    StudentCourse studentCourse = new StudentCourse();
    int beforeCount = sut.search().size();
    sut.updateStudentCourse(studentCourse);
    int afterCount = sut.search().size();
    assertThat(afterCount).isEqualTo(beforeCount);
  }
}
