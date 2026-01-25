package studio.thinkground.courseregistration.entity;

import jakarta.persistence.*;
import lombok.*;
import studio.thinkground.courseregistration.domain.Role;

@Table(name="students")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="student_id",updatable = false)
    private Long studentId;

    @Column(name="student_name",nullable = false)
    private String name;

    @Column(name="student_number",nullable = false,unique = true)
    private String studentNumber;

    @Column(name="password")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Student(String name,String studentNumber,String password,Role role)
    {
        this.name=name;
        this.studentNumber=studentNumber;
        this.password=password;
        this.role=role;
    }



}
