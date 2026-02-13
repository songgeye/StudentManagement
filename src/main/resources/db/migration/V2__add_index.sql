-- students_courses.student_id 検索用インデックス
CREATE INDEX idx_students_courses_student_id
  ON students_courses (student_id);

-- 外部キー制約
ALTER TABLE students_courses
  ADD CONSTRAINT fk_students_courses_student
  FOREIGN KEY (student_id)
  REFERENCES students (id)
  ON DELETE CASCADE;
