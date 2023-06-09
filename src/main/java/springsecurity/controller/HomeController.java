package springsecurity.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springsecurity.model.AuthenticationRequest;
import springsecurity.model.AuthenticationResponse;
import springsecurity.model.StudentDetail;
import springsecurity.sercurity.JwtTokenProvider;
import springsecurity.service.MyUserDetailService;

@RestController
public class HomeController {
    private final Logger log = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private MyUserDetailService userDetailService;

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
    public AuthenticationResponse login(@RequestBody AuthenticationRequest request){
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
        }
        // Nếu không xảy ra exception tức là thông tin hợp lệ
        // Set thông tin authentication vào Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        StudentDetail studentDetail = userDetailService.loadUserByUsername(request.getName());

        // Trả về jwt cho người dùng
        String jwt = tokenProvider.generateToken((StudentDetail) authentication.getPrincipal());
        return new AuthenticationResponse(jwt);
    }
}
