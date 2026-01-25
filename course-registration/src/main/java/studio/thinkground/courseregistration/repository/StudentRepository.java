package studio.thinkground.courseregistration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import studio.thinkground.courseregistration.entity.Student;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> // student 테이블을 Long id로
{
    Boolean existsByStudentNumber(String studentNumber);
    Student findByStudentNumber(String studentNumber);
}
//jparepository로 save,findbyid,findall,delete가능