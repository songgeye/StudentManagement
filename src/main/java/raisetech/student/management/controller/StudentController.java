package raisetech.student.management.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentsCourse;
import raisetech.student.management.service.StudentService;

@RestController
public class StudentController {

  private StudentService service;

  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  @GetMapping("/studentsList/thirties")
  public List<Student> getStudentsList() {
    return service.searchStudentList();
  }

  @GetMapping("/studentsCourseList/courses/java")
  public List<StudentsCourse> getStudentsCourseList() {
    return service.searchStudentsCourseList();
  }
}
