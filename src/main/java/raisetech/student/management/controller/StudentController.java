package raisetech.student.management.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.service.StudentService;

@RestController
public class StudentController {

  private StudentService service;

  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  /**
   * 30代の学生一覧を取得します（フィルタ専用エンドポイント）
   *
   * <p><strong>重要:</strong> このエンドポイントは全学生を返すのではなく、
   * 年齢が30-39歳の学生のみを返します。</p>
   *
   * <p>一般的なRESTful APIでは {@code /students?ageFrom=30&ageTo=39} のような
   * クエリパラメータを使用しますが、学習目的のため固定フィルタを適用しています。</p>
   *
   * @return 30代の学生一覧（該当者が存在しない場合は空のリスト）
   * @see StudentService#searchStudentList()
   */
  @GetMapping("/studentsList/thirties")
  public List<StudentDetail> getStudentsList() {
    List<Student> students = service.searchStudentList();
    List<StudentCourse> studentCourses = service.searchStudentsCourseList();

    List<StudentDetail> studentDetails = new ArrayList<>();
    for (Student student : students) {
      StudentDetail studentDetail = new StudentDetail();
      studentDetail.setStudent(student);
      for (StudentCourse studentCourse : studentCourses) {
        if (student.getId().equals(studentCourse.getStudentId())) {
          // 処理内容を記載
        }
      }
    }
    return
  }

  /**
   * Java基礎コースの受講生コース情報一覧を取得します（フィルタ専用エンドポイント）
   *
   * <p><strong>重要:</strong> このエンドポイントは全コース情報を返すのではなく、
   * 「Java基礎コース」のコース情報のみを返します。</p>
   *
   * <p>一般的なRESTful APIでは {@code /courses?courseName=Java基礎コース} のような
   * クエリパラメータを使用しますが、学習目的のため固定フィルタを適用しています。</p>
   *
   * @return Java基礎コースの受講生コース情報一覧（該当データが存在しない場合は空のリスト）
   * @see StudentService#searchStudentsCourseList()
   */
  @GetMapping("/studentsCourseList/courses/java")
  public List<StudentCourse> getStudentsCourseList() {
    return service.searchStudentsCourseList();
  }
}
