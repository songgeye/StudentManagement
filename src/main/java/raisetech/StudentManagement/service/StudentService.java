package raisetech.StudentManagement.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourse;
import raisetech.StudentManagement.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList() {
    // 年齢が30代の人のみ抽出し、リストをコントローラに返す
    List<Student> searchAllStudent = repository.search();

    return searchAllStudent.stream()
        .filter(student -> student.getAge() >= 30 && student.getAge() < 40)
        .collect(Collectors.toList());
  }

  public List<StudentsCourse> searchStudentsCourseList() {
    // 「Java基礎コース」のコース情報のみ抽出し、リストをコントローラに返す
    List<StudentsCourse> searchAllStudentCourses = repository.searchStudentCourses();

    return searchAllStudentCourses.stream()
        .filter(studentCourses -> "Java基礎コース".equals(studentCourses.getCourseName()))
        .collect(Collectors.toList());
  }
}
