package springsecurity.service;

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
        Student student = Mapper.studentRegToStudent(registerStudent);
//        Optional<Student> saved = Optional.of(save(student));
//        //tạo và lưu vertification nếu user được tạo
//        saved.ifPresent( u -> {
//            try {
//                //tự sinh 1 chuỗi token random
//                String token = UUID.randomUUID().toString();
//                //tiến hành tạo mới đối tượng vertification với student vừa tạo + token tự sinh
//                vertificationTokenService.save(saved.get(), token);
//                // gửi link vertification trong mail
//                emailService.sendEmail(u);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        });
//        return saved.get();
        save(student);
        Student stu = studentRepository.findById(student.getId()).orElse(null);
        if (stu!=null){
            try {
                String token = UUID.randomUUID().toString();
                vertificationTokenService.save(stu, token);
                emailService.sendEmail(stu);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return stu;
    }



















}
