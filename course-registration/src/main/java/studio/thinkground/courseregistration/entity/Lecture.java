package studio.thinkground.courseregistration.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name="Lecture")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
@Entity // 자동으로 DB에 들어감
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="lecture_id")//DB에서는 lecture_id라 쓰고 싶어서
    private Long id;

    private int grade;
    private String lectureName;
    private int credit;
    private String professor;
    private String time;
    private int max_student;

    private int currentCount=0;

    public Lecture(int grade, String lectureName, int credit, String professor, String time, int max_student) {
        this.grade = grade;
        this.lectureName = lectureName;
        this.credit = credit;
        this.professor = professor;
        this.time = time;
        this.max_student = max_student;
    }
    //수강신청 시 증가
    public void increaseCount()
    {
        if(this.currentCount>=max_student)
        {
            throw new IllegalArgumentException("이미 마감된 강의입니다");
        }
        this.currentCount++;
    }
    //강의 취소
    public void decreaseCount(){
        if(this.currentCount>0) {
            this.currentCount--;
        }
    }
}
