package springsecurity.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import springsecurity.model.Student;
import springsecurity.model.StudentDetail;
import springsecurity.repository.StudentRepository;

import java.util.Optional;

@Service
public class MyUserDetailService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(MyUserDetailService.class);

    private StudentRepository studentRepository;

    public MyUserDetailService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    //ghi đè phương thức truy vấn đến db lấy ra đối tượng được có username được truyê vào từ login
    @Override
    public StudentDetail loadUserByUsername(String name) throws UsernameNotFoundException {
        Optional<Student> student = null;
        try {
            student = studentRepository.findByName(name);
        }catch (UsernameNotFoundException e){
            log.error("NOT FOUND STUDENT", new UsernameNotFoundException(name));
        }
        //từ thông tin user truy vấn từ db lên sẽ tiến hành map nó thành object người dùng để đưa vào security quản lí
        return new StudentDetail(student.get());
//        return student.map(StudentDetail::new).get();
    }
}
