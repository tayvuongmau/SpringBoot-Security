package springsecurity.controller;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import springsecurity.model.Result;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(NullPointerException.class)
    public Result nullPointer(Exception ex){
        ex.printStackTrace();
        return new Result("404-NOT FOUND","");
    }
    @ExceptionHandler(AccessDeniedException.class)
    public Result accessDenied(Exception ex){
        ex.printStackTrace();
        return new Result("403-ACCESS DENIED","");
    }
    @ExceptionHandler(Exception.class)
    public Result exception(Exception e){
        e.printStackTrace();
        return new Result("500-BAD REQUEST","");
    }

    @ExceptionHandler(DisabledException.class)
    public Result disabledException(Exception e){
        e.printStackTrace();
        return new Result("Tài khoản chưa được kích hoạt","");
    }

}
