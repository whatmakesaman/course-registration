package studio.thinkground.courseregistration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import studio.thinkground.courseregistration.entity.Lecture;


public interface LectureRepository extends JpaRepository<Lecture,Long> {

}
