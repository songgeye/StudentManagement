package raisetech.student.management.service;

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
  private StudentCourse studentCourse;

  @BeforeEach
  void before() {
    sut = new StudentService(repository, converter);
    Student studentstu = new Student();
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

    StudentDetail studentDetailsut;
    studentDetailsut = new StudentDetail();
    student.setName(student.getName());
    student.setKanaName(student.getKanaName());
    student.setNickname(student.getNickname());
    student.setEmail(student.getEmail());
    student.setArea(student.getArea());
    student.setAge(student.getAge());
    student.setGender(student.getGender());
    Object studentDetailstu;
    studentDetailstu.setStudent();
    sut.registerStudent();
  }
}