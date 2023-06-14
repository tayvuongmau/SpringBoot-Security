package springsecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
public class HomeController {

    @Autowired
    MessageSource messageSource;

    @GetMapping("/")
    public String home(@RequestHeader(name="Accept-Language",required = false) Locale locale){
        String message = messageSource.getMessage("firstText", null, locale);
        return message;
    }
}
