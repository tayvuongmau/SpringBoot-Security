package springsecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.*;
import springsecurity.multi_language.LocaleConfig;

import java.util.Locale;

@RestController
public class HomeController {

    @Autowired
    MessageSource messageSource;

    //
    @GetMapping("/")
    public String home(){
        String mes = messageSource.getMessage("hello", null, LocaleContextHolder.getLocale());
        return mes;
    }
}
