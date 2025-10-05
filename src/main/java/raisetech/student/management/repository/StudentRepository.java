package raisetech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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
//      @Result(property = "area", column = "area"),
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

  @Insert(
      "INSERT INTO students "
          + "(name, kana_name, nickname, email, area, age, gender, remark, is_deleted) "
          + "VALUES(#{name}, #{kanaName}, #{nickname}, #{email}, #{area}, #{age}, #{gender}, #{remark}, false)")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void registerStudent(Student student);

  @Insert(
      "INSERT INTO students_courses "
          + "(student_id, course_name, course_start_at, course_end_at) "
          + "VALUES(#{studentId}, #{courseName}, #{courseStartAt}, #{courseEndAt})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void registerStudentCourse(StudentCourse studentCourse);

  @Update(
      "UPDATE students "
          + "SET name = #{name}, "
          + "kana_name = #{kanaName}, "
          + "nickname = #{nickname}, "
          + "email = #{email}, "
          + "area = #{area}, "
          + "age = #{age}, "
          + "gender = #{gender}, "
          + "remark = #{remark} "
          + "WHERE id = #{id}"
  )
  void updateStudent(Student student);

  /**
   * IDで受講生を1件検索する
   *
   * @param id 検索する受講生のID
   * @return 見つかった受講生(見つからない場合はnull)
   */
  @Select("SELECT * FROM students WHERE id = #{id}")
  Student searchStudent(Integer id);

  /**
   * 指定された受講生IDのコース情報を取得する
   *
   * @param studentId 受講生のID
   * @return その受講生のコース情報リスト
   */
  @Select("SELECT * FROM students_courses WHERE student_id = #{studentId}")
  List<StudentCourse> searchStudentCoursesByStudentId(Integer studentId);

  @Update(
      "UPDATE students_courses "
          + "SET course_name = #{courseName}, "
          + "course_start_at = #{courseStartAt}, "
          + "course_end_at = #{courseEndAt} "
          + "WHERE student_id = #{studentId}"
  )
  void updateStudentCourse(StudentCourse studentCourse);
}

