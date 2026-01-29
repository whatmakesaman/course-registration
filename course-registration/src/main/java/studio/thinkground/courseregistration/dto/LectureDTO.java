package studio.thinkground.courseregistration.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LectureDTO {
    //사용자가 입력하지 않는 값은 빠져야 함
    //id, currentStudent
    private String grade;
    private String lectureName;
    private int credit;
    private String professor;
    private String time;
    private int maxStudent;
}
