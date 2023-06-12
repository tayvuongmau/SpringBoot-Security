package springsecurity.util;

import springsecurity.model.RegisterStudent;
import springsecurity.model.Student;

public class Mapper {
    //chuyển từ dto registerStudent thành entity student
    public static Student studentRegToStudent(RegisterStudent registerStudent){
        Student student = new Student();
            student.setName(registerStudent.getName());
            student.setPassword(registerStudent.getPassword());
            student.setFullname(registerStudent.getFullname());
            student.setAge(registerStudent.getAge());
            student.setEmail(registerStudent.getEmail());
            student.setRole(registerStudent.getRole());
            student.setActivated(registerStudent.isActivated());
            return student;
    }
}
