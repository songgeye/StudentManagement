package raisetech.StudentManagement.service;

import java.util.List;
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
    // ここで何らかの処理を書く(検索処理)
    // 絞り込み(年齢が30代の人のみ絞り込みをする)
    // 抽出したリストをコントローラに返す
    return repository.search();
  }

  public List<StudentsCourse> searchStudentsCourseList() {
    // 絞り込み検索で「Javaコース」のコース情報のみ抽出
    // 抽出したリストをコントローラに返す
    return repository.searchStudentCourses();
  }
}
