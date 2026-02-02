package studio.thinkground.courseregistration.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import studio.thinkground.courseregistration.dto.LectureDTO;
import studio.thinkground.courseregistration.entity.Lecture;
import studio.thinkground.courseregistration.service.LectureService;

import java.util.List;

//타임리프를 쓰면 html안에 적힌 변수를 자바가 실제 값으로 바꿔야 함
@Controller
@RequiredArgsConstructor
public class MainController {

    private final LectureService lectureService;
    //localhost:8080/ 이라고 쳤을 때
    @GetMapping("/")
    public String root(){
        return "login";  //templates/main.html 보여줘라
    }
    //localhost:8080/login, 로그인 후 메인 화면
    @GetMapping("/main")
    public String mainPage(){
        return "main";
    }

    //회원가입 화면,    localhost:8080/join
    @GetMapping("/join")
    public String joinPage(){
        return "join";  // join.html을 보여줘라
    }


    //localhost:8080/login 접속 시
    @GetMapping("/login")
    public String loginPage(){
        return "login";

    }
    //학생 메인 페이지
    @GetMapping("/main")
    public String mainPage(Model model){

        //서비스에서 강의 목록 가져옴
        List<LectureDTO> lectures=lectureService.getAllLectures();
        //"lectures"이름 붙여서 html로 배달
        model.addAttribute("lectures",lectures);
        return "main";
    }

}
