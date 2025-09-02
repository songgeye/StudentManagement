package raisetech.StudentManagement;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import raisetech.StudentManagement.Student;
import raisetech.StudentManagement.StudentRepository;

@SpringBootApplication
@RestController
public class StudentManagementApplication {

	@Autowired
	private StudentRepository repository;

	private String name = "Matsugano Kengo";
	private String age = "31";

	public static void main(String[] args) {
		SpringApplication.run(StudentManagementApplication.class, args);
	}

	@GetMapping("/studentInfo")
	public String getStudentInfo() {
		Student student = repository.searchByName("OhtaniShouhei");
		return student.getName() + " " + student.getAge() + "歳";
	}

	@PostMapping("/studentInfo")
	public void setStudentInfo(String name, String age) {
		this.name = name;
		this.age = age;
	}

	@PostMapping("/studentName")
	public void updateStudentName(String name) {
		this.name = name;
	}
}