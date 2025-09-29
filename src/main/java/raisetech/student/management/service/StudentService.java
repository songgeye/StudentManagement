package raisetech.student.management.service;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;
  private StudentDetail detail;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList() {
    return repository.search();
  }

  public List<StudentCourse> searchStudentsCourseList() {
    return repository.searchStudentCourses();
  }

  public void registerStudent(Student newStudent) {
    String newId = UUID.randomUUID().toString();
    newStudent.setId(newId);
    int rows = repository.insertStudent(newStudent);
    if (rows != 1) {
      // 失敗時の扱い（例外投げる・ログ出す等）
      System.out.println("失敗しました");
    }
    System.out.println("成功しました");
  }

  public void processStudentDetail(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();
  }
}
