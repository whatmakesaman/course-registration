package studio.thinkground.courseregistration.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import studio.thinkground.courseregistration.dto.JoinDTO;
import studio.thinkground.courseregistration.service.JoinService;

@Controller
@ResponseBody
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;

    @PostMapping("/join/admin")
    public String joinAdminProcess(JoinDTO joinDTO)
    {
        joinService.joinAdmin(joinDTO);
        return "관리자 완료";
    }
    @PostMapping("/join/student")
    public String joinStudentProcess(JoinDTO joinDTO)
    {
        joinService.joinStudent(joinDTO);
        return "학생 완료";
    }


}
