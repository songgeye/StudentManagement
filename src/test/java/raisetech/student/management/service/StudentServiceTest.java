package raisetech.student.management.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter converter;

  private StudentService sut;
  private Student student;
  private List<StudentCourse> studentCourseList;
  private StudentDetail studentDetail;

  @BeforeEach
  void before() {
    sut = new StudentService(repository, converter);
    student = new Student();
    student.setName("テスト太郎");
    student.setKanaName("テストタロウ");
    student.setEmail("test@example.com");
    student.setArea("東京");
    student.setAge(25);
    student.setGender("M");

    studentCourseList = new ArrayList<>();

    StudentCourse firstCourse = new StudentCourse();
    firstCourse.setCourseName("Javaフルコース");
    studentCourseList.add(firstCourse);

    StudentCourse secondCourse = new StudentCourse();
    secondCourse.setCourseName("フロントエンドコース");
    studentCourseList.add(secondCourse);

    studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setStudentsCourseList(studentCourseList);
  }

  @Test
  void 受講生詳細の一覧検索_リポジトリとコンバーターの処理が適切に呼び出せていること() {
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    when(repository.search()).thenReturn(studentList);
    when(repository.searchStudentCourseList()).thenReturn(studentCourseList);

    sut.searchStudentList();

    verify(repository, times(1)).search();
    verify(repository, times(1)).searchStudentCourseList();
    verify(converter, times(1)).convertStudentDetails(studentList, studentCourseList);
  }

  @Test
  void 受講生詳細の登録_受講生と受講生コース情報を個別に登録し受講生コース情報には受講生情報を紐づける値とコース開始日とコース終了日を設定できていること() {
    doAnswer(
        invocation -> {
          Student s = invocation.getArgument(0);
          s.setId("1");
          return null;
        }
    ).when(repository).registerStudent(any(Student.class));

    StudentDetail result = sut.registerStudent(studentDetail);

    verify(repository).registerStudent(student);
    verify(repository, times(studentCourseList.size())).registerStudentCourse(
        any(StudentCourse.class));

    for (StudentCourse studentCourse : result.getStudentsCourseList()) {
      assertEquals(student.getId(), studentCourse.getStudentId());
      assertNotNull(studentCourse.getCourseStartAt());
      assertNotNull(studentCourse.getCourseEndAt());
    }

    assertSame(studentDetail, result);
  }
}