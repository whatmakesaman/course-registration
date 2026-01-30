package studio.thinkground.courseregistration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import studio.thinkground.courseregistration.entity.Enrollment;
import studio.thinkground.courseregistration.entity.Lecture;
import studio.thinkground.courseregistration.entity.Student;

public interface EnrollmentRepository extends JpaRepository<Enrollment,Long> {

    boolean existsByStudentAndLecture(Student student, Lecture lecture);
}
