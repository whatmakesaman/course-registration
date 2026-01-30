package studio.thinkground.courseregistration.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import studio.thinkground.courseregistration.service.EnrollmentService;

@Controller
@RequiredArgsConstructor
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    @PostMapping("/enrollment")
    public String enroll(@RequestParam Long studentId,@RequestParam Long lectureId)
    {
        enrollmentService.enroll(studentId, lectureId);
        return "강의가 추가되었습니다";
    }

}
