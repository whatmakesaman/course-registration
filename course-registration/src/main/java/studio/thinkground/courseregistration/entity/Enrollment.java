package studio.thinkground.courseregistration.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name="enrollment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="enrollmentId")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="lecture_id")//lecture의 Id 컬럼과 맞춤
    private Lecture lecture;

    //student로 하면 member와 student를 조회해서 변환하는 과정이 추가, 어차피 로그인할 때 member로 로그인
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="member_id")
    @JsonIgnore
    private Member member;
    //private Student student;

    public Enrollment(Lecture lecture, Member member) {
        this.lecture = lecture;
        this.member=member;
    }
}
