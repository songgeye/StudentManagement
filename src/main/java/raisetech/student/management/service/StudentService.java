package raisetech.student.management.service;

import java.util.List;
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

  public List<Student> registerStudent(Student newStudent) {
    return repository.search();
  }

  public void processStudentDetail(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();
  }
}
