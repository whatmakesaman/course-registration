package studio.thinkground.courseregistration.dto;

import lombok.*;
import studio.thinkground.courseregistration.entity.Lecture;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LectureDTO {
    //사용자가 입력하지 않는 값은 빠져야 함

    private Long id;

    private int grade;
    private String lectureName;
    private int credit;
    private String professor;
    private String time;
    private int maxStudent;
    private int currentCount;

    public LectureDTO(Lecture lecture)
    {
        this.id= lecture.getId();

        this.lectureName = lecture.getLectureName();
        this.professor = lecture.getProfessor();
        this.credit = lecture.getCredit();
        this.grade = lecture.getGrade();
        this.time = lecture.getTime();
        this.maxStudent = lecture.getMax_student();
        this.currentCount= lecture.getCurrentCount();
    }
}
