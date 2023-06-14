package springsecurity.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springsecurity.model.*;
import springsecurity.sercurity.JwtTokenProvider;
import springsecurity.service.StudentService;


import javax.validation.Valid;


@RestController
@RequestMapping("/accounts")
public class AccountController {

    private StudentService studentService;
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider tokenProvider;

    public AccountController(StudentService studentService, AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider) {
        this.studentService = studentService;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @GetMapping("/access-denied")
    public ResponseEntity<Result> accessDenied(){
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new Result("403","Yêu cầu thất bại do bạn không có quyền truy cập hoặc tài khoản chưa kích hoạt"));
    }

    @PostMapping("/register")
    public ResponseEntity<Result> register(@RequestBody @Valid RegisterStudent registerStudent,
                                           BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            studentService.valid(bindingResult);
        }
        Student student = studentService.register(registerStudent);
        if (student==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Result("Username already exist.",""));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new Result("Please check your mailbox to active your account.",""));
    }


    @GetMapping("/activation")
    public ResponseEntity<Result> activation(@RequestParam("token") String token){
        return studentService.checkVerificationToken(token);
    }

    @PostMapping("/login")
    public ResponseEntity<Result> login(@RequestBody AuthenticationRequest request){
        return studentService.login(request);
    }
}
