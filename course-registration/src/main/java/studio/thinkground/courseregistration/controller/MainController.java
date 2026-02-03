package studio.thinkground.courseregistration.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import studio.thinkground.courseregistration.dto.LectureDTO; // DTO 사용 확인
import studio.thinkground.courseregistration.service.EnrollmentService;
import studio.thinkground.courseregistration.service.LectureService;

import java.util.ArrayList; // 리스트 사용을 위해 추가
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final LectureService lectureService;
    private final EnrollmentService enrollmentService;

    @GetMapping("/join")
    public String joinPage(){
        return "join";
    }

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @GetMapping("/main")
    public String mainPage(Model model){

        // 1. 전체 강의 목록 가져오기
        List<LectureDTO> lectures = lectureService.getAllLectures();
        model.addAttribute("lectures", lectures);

        // 2. 현재 로그인한 사용자 아이디 확인
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loginId = auth.getName();

        // 3. 내 신청 목록 가져오기 (안전 장치 추가)
        List<LectureDTO> myLectures = new ArrayList<>(); // 일단 빈 리스트로 초기화

        if(loginId != null && !loginId.equals("anonymousUser")) {
            try {
                // 서비스에서 목록 가져오기 시도
                myLectures = enrollmentService.getMyLectures(loginId);
            } catch(Exception e) {
                // 에러가 나면 콘솔에 이유를 출력하고, myLectures는 빈 리스트인 채로 둠
                e.printStackTrace();
                System.out.println("내 강의 목록 불러오기 실패: " + e.getMessage());
            }
        }

        // ★ [핵심] 리스트가 비어있어도 모델에 꼭 넣어야 500 에러가 안 남!
        model.addAttribute("myLectures", myLectures);

        return "main";
    }
}