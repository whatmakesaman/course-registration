package studio.thinkground.courseregistration.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import studio.thinkground.courseregistration.dto.LectureDTO;
import studio.thinkground.courseregistration.service.LectureService;

@RestController
@RequiredArgsConstructor
public class LectureAdminController {

    private final LectureService lectureService;

    @PostMapping("/admin/lecture/add")
    public String addLecture(@RequestBody LectureDTO lectureDTO)
    {
        lectureService.createLecture(lectureDTO);
        return "강의 개설 완료:"+lectureDTO.getLectureName();
    }
}
