package studio.thinkground.courseregistration.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class StudentController {

    @GetMapping("/")//config에서 permitall
    public String mainP(){
        return "Student Controller";
    }
}
