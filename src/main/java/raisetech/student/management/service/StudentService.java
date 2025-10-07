package raisetech.student.management.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;

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

  // 複数のデータベース操作を1つのトランザクションとしてまとめるアノテーション
  @Transactional
  public void registerStudent(StudentDetail studentDetail) {
    // 受講生の基本情報を登録(students テーブル)
    repository.registerStudent(studentDetail.getStudent());
    // studentDetail.getStudent()
    //    ↓
    // Student { id: null, name: "山田太郎", age: 25, ... }
    //    ↓
    // repository.registerStudent(student)
    //    ↓
    // INSERT INTO students(...) VALUES(...)
    //    ↓
    // データベースが自動でIDを生成(例: 123)
    //    ↓
    // @Options(useGeneratedKeys = true) により
    // studentオブジェクトのidに123が自動セット
    //    ↓
    // Student { id: 123, name: "山田太郎", age: 25, ... }
    // 登録時に自動生成されたIDを取得
    for (StudentCourse studentCourse : studentDetail.getStudentCourse()) {
      // 受講生IDをセット
      studentCourse.setStudentId(
          studentDetail.getStudent().getId());
      // 開始日・終了日をセット
      studentCourse.setCourseStartAt(LocalDateTime.now());
      studentCourse.setCourseEndAt(LocalDateTime.now().plusYears(1));
      repository.registerStudentCourse(studentCourse);
    }
  }

  public StudentDetail searchStudent(int id) {
    // ①リポジトリで受講生を検索
    Student student = repository.searchStudent(id);
    // どちらでもOKだが、idの方がシンプル
    //    repository.searchStudentCoursesByStudentId(id);  // ← こっちでOK
    //    repository.searchStudentCoursesByStudentId(student.getId());  // これも同じ
    // ②リポジトリでその人のコースを検索
    List<StudentCourse> studentCourses = repository.searchStudentCoursesByStudentId(id);
    // ③StudentDetailに詰めて返す
    StudentDetail studentDetail = new StudentDetail();
    // ④ 受講生情報をセット
    studentDetail.setStudent(student);
    // ⑤ コース情報をセット
    studentDetail.setStudentCourse(studentCourses);
    // ⑥ 完成したStudentDetailを返す
    return studentDetail;
  }

  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    // ①受講生情報を更新
    repository.updateStudent(studentDetail.getStudent());
    // ②コース情報を更新(forループで全コースを更新)
    for (StudentCourse studentCourse : studentDetail.getStudentCourse()) {
      studentCourse.setStudentId(
          studentDetail.getStudent().getId());
      if (studentCourse.getId() == null) {
        repository.registerStudentCourse(studentCourse);
      } else {
        repository.updateStudentCourse(studentCourse);
      }
    }
  }
}