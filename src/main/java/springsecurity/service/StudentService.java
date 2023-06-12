package springsecurity.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import springsecurity.model.RegisterStudent;
import springsecurity.model.Student;
import springsecurity.repository.StudentRepository;
import springsecurity.util.Mapper;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
public class StudentService {
    private StudentRepository studentRepository;
    private VertificationTokenService vertificationTokenService;
    private EmailService emailService;

    private final Logger log = LoggerFactory.getLogger(StudentService.class);

    public StudentService(StudentRepository studentRepository,
                          VertificationTokenService vertificationTokenService,
                          EmailService emailService) {
        this.studentRepository = studentRepository;
        this.vertificationTokenService = vertificationTokenService;
        this.emailService = emailService;
    }

    @Transactional
    public Student save(Student student){
        Student stu = studentRepository.save(student);
        return stu;
    }

    @Transactional
    public Student register(RegisterStudent registerStudent){
        Student stu = studentRepository.findByName(registerStudent.getName()).orElse(null);
        if (stu!=null){
            log.error("Ten tai khoan da ton tai");
            return null;
        }
        Student student = Mapper.studentRegToStudent(registerStudent);
        save(student);
        if (student!=null){
            try {
                String token = UUID.randomUUID().toString();
                vertificationTokenService.save(student, token);
                emailService.sendEmail(student);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return student;
    }



















}
