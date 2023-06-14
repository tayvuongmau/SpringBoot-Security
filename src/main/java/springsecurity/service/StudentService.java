package springsecurity.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import springsecurity.model.*;
import springsecurity.repository.StudentRepository;
import springsecurity.sercurity.JwtTokenProvider;
import springsecurity.util.Mapper;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class StudentService {
    private StudentRepository studentRepository;
    private VertificationTokenService vertificationTokenService;

    private EmailService emailService;
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider tokenProvider;
    private MessageSource messageSource;

    @Autowired
    public StudentService(StudentRepository studentRepository, VertificationTokenService vertificationTokenService,
                          EmailService emailService, AuthenticationManager authenticationManager,
                          JwtTokenProvider tokenProvider, MessageSource messageSource) {
        this.studentRepository = studentRepository;
        this.vertificationTokenService = vertificationTokenService;
        this.emailService = emailService;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.messageSource = messageSource;
    }
    private final Logger log = LoggerFactory.getLogger(StudentService.class);

    public StudentService() {

    }

    @Transactional
    public Student save(Student student){
        Student stu = studentRepository.save(student);
        return stu;
    }

    @Transactional
    public Student register(RegisterStudent registerStudent){
        //kiểm tra tài khoản đã tồn tại chưa
        Student stu = studentRepository.findStudentByNameAndPassword(registerStudent.getName(), registerStudent.getPassword());
        if (stu!=null){
            log.error("Tên tài khoản ã tồn tại");
            return null;
        }
        //chuyên dữ liệu của student từ dto => entity và lưu vào db
        Student student = Mapper.studentRegToStudent(registerStudent);
        save(student);
        if (student!=null){
            try {
                //tạo ra một chuỗi token ngẫu nhiên dùng xác thực
                String token = UUID.randomUUID().toString();
                //lưu dữ liệu của tài khoản và token vừa tạo ra vào bảng verification_token
                vertificationTokenService.save(student, token);
                //tiến hành gửi mail
                emailService.sendEmail(student);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return student;
    }

    public ResponseEntity<Result> valid(BindingResult bindingResult){
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(
                    error->errors.put(error.getField(),error.getDefaultMessage())
            );
            String msgErr ="";
            for (String key : errors.keySet()){
                msgErr += "Lỗi ở trường: " + key + ", lí do: " + errors.get(key) + "\n";
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Result(messageSource.getMessage("created-fail",null, LocaleContextHolder.getLocale()),msgErr));
    }

    public ResponseEntity<Result> checkVerificationToken(String token){
        VerificationToken verificationToken = vertificationTokenService.findByToken(token);
        if (verificationToken==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Result(messageSource.getMessage("verification-token-invalid",null, LocaleContextHolder.getLocale()),""));
        }else {
            Student student = verificationToken.getStudent();
            //nếu student chưa active
            if(!student.isActivated()){
                //lấy ra thời gian hiện tại
                Timestamp curentTimestamp = new Timestamp(System.currentTimeMillis());
                //kiểm tra thời gian tồn tại của token
                if(verificationToken.getExpiryDate().before(curentTimestamp)){
                    return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT)
                            .body(new Result(messageSource.getMessage("expiry-date",null, LocaleContextHolder.getLocale()),""));
                }else {
                    //set lại trạng thái kích hoạt
                    student.setActivated(true);
                    save(student);
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(messageSource.getMessage("activated-success",null, LocaleContextHolder.getLocale()),""));
    }

    public ResponseEntity<Result> login(AuthenticationRequest request){
        Authentication authentication = null;
        try {
            //xác thực từ username và password nhập vào
            Student student = studentRepository.findStudentByNameAndPassword(request.getName(), request.getPassword());
            if (student==null){
                log.error("Thông tin đăng nhập không đúng");
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new Result(messageSource.getMessage("info-false",null, LocaleContextHolder.getLocale()),"Gõ sai tài khoản rồi con lợn"));
            }
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getName(),
                            request.getPassword()
                    )
            );
        }catch (BadCredentialsException e){
            e.printStackTrace();
        }
        // Nếu không xảy ra exception tức là thông tin hợp lệ
        // Set thông tin authentication vào Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Trả về jwt cho người dùng
        String jwt = tokenProvider.generateToken((StudentDetail) authentication.getPrincipal());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Result(messageSource.getMessage("login-success",null, LocaleContextHolder.getLocale()),jwt));
    }
















}
