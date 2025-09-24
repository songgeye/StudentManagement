package raisetech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;

/**
 * 受講生情報を扱うリポジトリ
 * <p>
 * 全件検索や単一条件での検索、コース情報の検索が行えるクラスです。
 */
@Mapper
public interface StudentRepository {

  /**
   * 全件検索をします。
   *
   * @return 全件検索をした受講生情報の一覧
   */
  @Select("SELECT * FROM students")
//  @Results({
//      @Result(property = "id", column = "id"),
//      @Result(property = "name", column = "name"),
//      @Result(property = "kanaName", column = "kana_name"),
//      @Result(property = "nickname", column = "nickname"),
//      @Result(property = "email", column = "email"),
//      @Result(property = "address", column = "address"),
//      @Result(property = "age", column = "age"),
//      @Result(property = "gender", column = "gender")
//  })
  List<Student> search();

  @Select("SELECT * FROM students_courses")
//  @Results({
//      @Result(property = "id", column = "id"),
//      @Result(property = "studentId", column = "student_id"),
//      @Result(property = "courseName", column = "course_name"),
//      @Result(property = "courseStartAt", column = "course_start_at"),
//      @Result(property = "courseEndAt", column = "course_end_at")
//  })
  List<StudentCourse> searchStudentCourses();

  @Insert("""
      INSERT INTO students 
      (id, name) 
      VALUES 
      (#{id}, #{name})
      """)
  int insertStudent(Student student);
}