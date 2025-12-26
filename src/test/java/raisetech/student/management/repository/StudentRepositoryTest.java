package raisetech.student.management.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
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
  void 受講生の検索が行えること() {
    String id = "1";
    Student actual = sut.searchStudent(id);
    assertThat(actual.getId()).isEqualTo("1");
  }

  @Test
  void 受講生のコース情報の全件検索を行えること() {
    List<StudentCourse> actual = sut.searchStudentCourseList();
    assertThat(actual.size()).isEqualTo(8);
  }

  @Test
  void 受講生IDに紐づく受講生コース情報の検索が行えること() {
    String studentId = "1";
    List<StudentCourse> actual = sut.searchStudentCourse(studentId);
    assertThat(actual.get(1).getId()).isEqualTo("2");
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
  void 受講生コースの登録が行えること() {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setStudentId("1");
    studentCourse.setCourseName("Javaコース");
    studentCourse.setCourseStartAt(LocalDateTime.now());
    studentCourse.setCourseEndAt(LocalDateTime.now().plusYears(1));

    sut.registerStudentCourse(studentCourse);

    List<StudentCourse> actual = sut.searchStudentCourseList();

    assertThat(actual.size()).isEqualTo(9);
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
  void 受講生コースの更新が行えること() {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setStudentId("2");
    studentCourse.setCourseName("AWSコース");
    studentCourse.setCourseStartAt(LocalDateTime.now());
    studentCourse.setCourseEndAt(LocalDateTime.now().plusYears(1));

    sut.updateStudentCourse(studentCourse);

    List<StudentCourse> actual = sut.searchStudentCourseList();

    assertThat(actual.size()).isEqualTo(8);
  }
}