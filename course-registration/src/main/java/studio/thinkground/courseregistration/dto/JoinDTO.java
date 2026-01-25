package studio.thinkground.courseregistration.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.ResponseBody;

@Getter
@Setter
@ResponseBody
public class JoinDTO {
    private String id; // 변수명만 id, 실제로 studentNumber,loginId
    private String password;
}
