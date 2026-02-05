package studio.thinkground.courseregistration.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import studio.thinkground.courseregistration.dto.LectureDTO;
import studio.thinkground.courseregistration.entity.Lecture;
import studio.thinkground.courseregistration.repository.LectureRepository;
import studio.thinkground.courseregistration.service.LectureService;

import java.util.List;
import java.util.stream.Collectors;

@Controller // RestController는 json/text만 반환, controller는 페이지를 이동
@RequiredArgsConstructor
public class LectureAdminController {

    private final LectureService lectureService;

    // 1. 강의 목록 조회 (GET)
    @GetMapping("/admin/lectures") //admin 요청을 받음
    public String getLectureListPage(Model model)
    {
        List<LectureDTO> lectures=lectureService.getAllLectures();
        //모델에 데이터 담기, lectures라는 이름표 붙여서 html로 보냄
        model.addAttribute("lectures",lectures);
        //화면으로 이동
        return "admin";//html화면 보여줌
    }

    // 2. 강의 등록 (POST)
    @PostMapping("/admin/lectures")
    @ResponseBody
    public String addLecture(@RequestBody LectureDTO lectureDTO) {
        // 서비스야, 이거 등록해라. (지시만 함)
        lectureService.createLecture(lectureDTO);

        //admin.html 파일을 주는 것이 아니라 /admin주소로 다시 접속해라(get 방식)
        return "ok";
    }

}
