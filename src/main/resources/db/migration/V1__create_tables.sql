CREATE TABLE students (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL,
  kana_name VARCHAR(50) NOT NULL,
  nickname VARCHAR(50),
  email VARCHAR(50) NOT NULL,
  area VARCHAR(50) NOT NULL,
  age INT,
  gender ENUM('M','F','O') DEFAULT NULL COMMENT 'M:男性, F:女性, O:その他/未回答',
  remark VARCHAR(255),
  is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
  PRIMARY KEY (id),
  UNIQUE KEY email (email)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE students_courses (
  id BIGINT NOT NULL AUTO_INCREMENT,
  student_id BIGINT NOT NULL,
  course_name VARCHAR(50) NOT NULL,
  course_start_at TIMESTAMP NULL,
  course_end_at TIMESTAMP NULL,
  application_status VARCHAR(20) NOT NULL DEFAULT 'TEMPORARY',
  PRIMARY KEY (id)
) ENGINE=InnoDB
    DEFAULT CHARSET=utf8mb4
    COLLATE=utf8mb4_0900_ai_ci;