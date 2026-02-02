package studio.thinkground.courseregistration.dto;

import lombok.Getter;
import lombok.Setter;
import studio.thinkground.courseregistration.entity.Member;

@Getter
@Setter
public class EnrollmentDTO {
    private String lecture;
    private Member member;

}
