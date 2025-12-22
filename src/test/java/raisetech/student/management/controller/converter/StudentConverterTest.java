package raisetech.student.management.controller.converter;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;

public class StudentConverterTest {

  private StudentConverter converter = new StudentConverter();

  @Test
  void 受講生詳細の受講生に複数のコースが紐づくこと() {
    Student student = new Student();
    student.setId("1");
    student.setName("松ヶ野健吾");
    student.setKanaName("マツガノケンゴ");
    student.setNickname("そん");
    student.setEmail("test@example.com");
    student.setArea("神奈川県");
    student.setGender("M");

    List<Student> studentList = List.of(student);

    StudentCourse firstCourse = new StudentCourse();
    firstCourse.setStudentId("1");
    firstCourse.setCourseName("WordPressコース");
    firstCourse.setCourseStartAt(LocalDateTime.parse("2025-12-07T00:00:00"));
    firstCourse.setCourseEndAt(LocalDateTime.parse("2026-12-07T00:00:00"));

    StudentCourse secondCourse = new StudentCourse();
    secondCourse.setStudentId("1");
    secondCourse.setCourseName("Javaコース");
    secondCourse.setCourseStartAt(LocalDateTime.parse("2025-12-22T00:00:00"));
    secondCourse.setCourseEndAt(LocalDateTime.parse("2026-12-22T00:00:00"));

    List<StudentCourse> courseList = List.of(firstCourse, secondCourse);

    List<StudentDetail> studentDetails = converter.convertStudentDetails(studentList, courseList);

    assertThat(studentDetails.size()).isEqualTo(1);
    assertThat(studentDetails.get(0).getStudent().getId()).isEqualTo("1");
    assertThat(studentDetails.get(0).getStudentsCourseList().size()).isEqualTo(2);
    assertThat(studentDetails.get(0).getStudentsCourseList().get(0).getCourseName()).isEqualTo(
        "WordPressコース");
    assertThat(studentDetails.get(0).getStudentsCourseList().get(1).getCourseName()).isEqualTo(
        "Javaコース");
  }

  @Test
  void 受講生詳細の受講生リストが空の場合に空のリストが返ること() {
    List<Student> studentList = List.of();
    List<StudentCourse> courseList = List.of();

    List<StudentDetail> studentDetails = converter.convertStudentDetails(studentList, courseList);

    assertThat(studentDetails.size()).isEqualTo(0);
  }
}
