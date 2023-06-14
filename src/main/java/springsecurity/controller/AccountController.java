package springsecurity.controller;


import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
    private MessageSource messageSource;

    public AccountController(StudentService studentService, MessageSource messageSource) {
        this.studentService = studentService;
        this.messageSource = messageSource;
    }

    @GetMapping("/access-denied")
    public ResponseEntity<Result> accessDenied(){
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new Result("403",messageSource.getMessage("access-denied",null, LocaleContextHolder.getLocale())));
    }

    @PostMapping("/register")
    public ResponseEntity<Result> register(@RequestBody @Valid RegisterStudent registerStudent,
                                           BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            studentService.valid(bindingResult);
        }
        Student student = studentService.register(registerStudent);
        if (student==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Result(messageSource.getMessage("user-exist",null, LocaleContextHolder.getLocale()),""));
        }
        return ResponseEntity.status(HttpStatus.CREATED).
                body(new Result(messageSource.getMessage("check-mail",null, LocaleContextHolder.getLocale()),""));
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
