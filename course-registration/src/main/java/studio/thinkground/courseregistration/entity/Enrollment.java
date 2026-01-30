package studio.thinkground.courseregistration.entity;


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

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="student_id")
    private Student student;

    public Enrollment(Lecture lecture, Student student) {
        this.lecture = lecture;
        this.student = student;
    }
}
