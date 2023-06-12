package springsecurity.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springsecurity.model.*;
import springsecurity.sercurity.JwtTokenProvider;
import springsecurity.service.MyUserDetailService;
import springsecurity.service.StudentService;
import springsecurity.service.VertificationTokenService;
import springsecurity.util.Mapper;

import java.sql.Timestamp;

@RestController
public class HomeController {
    private final Logger log = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private VertificationTokenService vertificationTokenService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private StudentService studentService;

    @GetMapping("/")
    public String home(){
        return ("<h1>HOME PAGE</h1>");
    }

    @GetMapping("/admin")
    public String admin(){
        return ("<h1>ADMIN PAGE</h1>");
    }

    @GetMapping("/user")
    public String user(){
        return ("<h1>USER PAGE</h1>");
    }

    //api này được gọi tới khi đăng nhập rồi sinh ra và trả lại jwt cho client
    @PostMapping("/login")
    public ResponseEntity<Result> login(@RequestBody AuthenticationRequest request){
        Authentication authentication = null;
        try {
            //xác thực từ username và password nhập vào
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getName(),
                            request.getPassword()
                    )
            );
        }catch (BadCredentialsException e){
            log.error("Thông tin đăng nhập không đúng", new IllegalArgumentException());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result("Sai thông tin đăng nhập",""));
        }
        // Nếu không xảy ra exception tức là thông tin hợp lệ
        // Set thông tin authentication vào Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Trả về jwt cho người dùng
        String jwt = tokenProvider.generateToken((StudentDetail) authentication.getPrincipal());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Result("Đăng nhập thành công",new AuthenticationResponse(jwt)));
    }

    @PostMapping("/register")
    public ResponseEntity<Result> register(@RequestBody RegisterStudent registerStudent){
        Student student = studentService.register(registerStudent);
        if (student==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Result("Username already exist.",""));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new Result("Please check your mailbox to active your account.",""));
    }


    @GetMapping("/activation")
    public ResponseEntity<Result> activation(@RequestParam("token") String token){
        VerificationToken verificationToken = vertificationTokenService.findByToken(token);
        if (verificationToken==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result("Your verification token is invalid",""));
        }else {
            Student student = verificationToken.getStudent();
            //nếu student chưa active
            if(!student.isActivated()){
                //lấy ra thời gian hiện tại
                Timestamp curentTimestamp = new Timestamp(System.currentTimeMillis());
                //kiểm tra thời gian tồn tại của token
                if(verificationToken.getExpiryDate().before(curentTimestamp)){
                    return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(new Result("Đã hết thời gia kích hoạt",""));
                }else {
                    //set lại trạng thái kích
                    student.setActivated(true);
                    studentService.save(student);
                }
            }
        }
        //thêm /activation vào security config
        return ResponseEntity.status(HttpStatus.OK).body(new Result("Your account has been activated.",""));
    }



































}
