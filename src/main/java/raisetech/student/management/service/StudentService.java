package raisetech.student.management.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList() {
    // 年齢が30代の人のみ抽出し、リストをコントローラに返す
    List<Student> allStudents = repository.search();

    if (allStudents == null) {
      return Collections.emptyList();
    }

    return allStudents.stream()
        .filter(student -> student != null)
        .filter(student -> student.getAge() >= 30 && student.getAge() < 40)
        .collect(Collectors.toList());
  }

  public List<StudentCourse> searchStudentsCourseList() {
    // 「Java基礎コース」のコース情報のみ抽出し、リストをコントローラに返す
    List<StudentCourse> allCourses = repository.searchStudentCourses();

    if (allCourses == null) {
      return Collections.emptyList();
    }

    return allCourses.stream()
        .filter(course -> course != null)
        .filter(studentCourses -> "Java基礎コース".equals(studentCourses.getCourseName()))
        .collect(Collectors.toList());
  }

  public List<Student> searchAllStudents() {
    List<Student> allStudents = repository.search();
    return allStudents != null ? allStudents : Collections.emptyList();
  }

  public List<StudentCourse> searchAllStudentsCourses() {
    List<StudentCourse> allCourses = repository.searchStudentCourses();
    return allCourses != null ? allCourses : Collections.emptyList();
  }
}
