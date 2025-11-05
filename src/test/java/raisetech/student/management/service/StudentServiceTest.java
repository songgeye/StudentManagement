package raisetech.student.management.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
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
import raisetech.student.management.exception.NotFoundException;
import raisetech.student.management.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter converter;

  private StudentService sut;

  @BeforeEach
  void before() {
    sut = new StudentService(repository, converter);
  }

  @Test
  void 受講生詳細の一覧検索_リポジトリとコンバーターの処理が適切に呼び出せていること() {
    // 準備
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    when(repository.search()).thenReturn(studentList);
    when(repository.searchStudentCourseList()).thenReturn(studentCourseList);

    // 実行
    sut.searchStudentList();

    // 検証
    verify(repository).search();
    verify(repository).searchStudentCourseList();
    verify(converter).convertStudentDetails(studentList, studentCourseList);
  }

  @Test
  void 受講生詳細の検索_リポジトリのsearchStudentとsearchStudentCourseメソッドが正しく呼び出されること() {
    // 準備
    String testId = "1";
    Student searchStudent = new Student();
    searchStudent.setId(testId);
    List<StudentCourse> studentCourseList = new ArrayList<>();

    when(repository.searchStudent(testId)).thenReturn(searchStudent);
    when(repository.searchStudentCourse(testId)).thenReturn(studentCourseList);

    // 実行
    StudentDetail result = sut.searchStudent(testId);

    // 検証
    verify(repository).searchStudent(testId);
    verify(repository).searchStudentCourse(testId);
    assertEquals(searchStudent, result.getStudent());
    assertEquals(studentCourseList, result.getStudentsCourseList());
  }

  @Test
  void 受講生詳細の検索_存在しないIDの場合は例外がスローされること() {
    // 準備
    String nonExistingId = "999";
    when(repository.searchStudent(nonExistingId)).thenReturn(null);

    // 実行と検証
    NotFoundException exception = assertThrows(NotFoundException.class,
        () -> sut.searchStudent(nonExistingId));

    // 追加検証
    assertEquals("IDが未定義です: " + nonExistingId, exception.getMessage());
    verify(repository).searchStudent(nonExistingId);
    verify(repository, never()).searchStudentCourse(nonExistingId);
  }

  @Test
  void 受講生詳細の登録_受講生と受講生コース情報を個別に登録し受講生コース情報には受講生情報を紐づける値とコース開始日とコース終了日を設定できていること() {
    // 準備
    Student student = new Student();
    StudentCourse firstCourse = new StudentCourse();

    List<StudentCourse> studentCourseList = new ArrayList<>();
    studentCourseList.add(firstCourse);

    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setStudentsCourseList(studentCourseList);

    doAnswer(invocation -> {
      Student s = invocation.getArgument(0);
      s.setId("1");
      return null;
    }).when(repository).registerStudent(any(Student.class));

    // 実行
    StudentDetail result = sut.registerStudent(studentDetail);

    // 検証
    verify(repository).registerStudent(student);
    verify(repository).registerStudentCourse(firstCourse);

    result.getStudentsCourseList().forEach(course -> {
      assertEquals(student.getId(), course.getStudentId());
      assertNotNull(course.getCourseStartAt());
      assertNotNull(course.getCourseEndAt());
    });

    assertSame(studentDetail, result);
  }

  @Test
  void 受講生詳細の更新＿リポジトリの更新メソッドが適切に呼び出されること() {
    // 準備
    Student updateStudent = new Student();
    updateStudent.setId("1");

    StudentCourse updateFirstCourse = new StudentCourse();
    updateFirstCourse.setId("10");
    updateFirstCourse.setStudentId("1");

    List<StudentCourse> updateCourseList = new ArrayList<>();
    updateCourseList.add(updateFirstCourse);

    StudentDetail updateStudentDetail = new StudentDetail();
    updateStudentDetail.setStudent(updateStudent);
    updateStudentDetail.setStudentsCourseList(updateCourseList);

    // 実行
    sut.updateStudent(updateStudentDetail);

    // 検証
    verify(repository).updateStudent(updateStudent);
    verify(repository).updateStudentCourse(updateFirstCourse);
  }
}