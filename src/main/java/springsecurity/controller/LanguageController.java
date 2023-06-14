package springsecurity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LanguageController {

    @GetMapping("/page")
    public String languagePage(){
        return "international";
    }
}
