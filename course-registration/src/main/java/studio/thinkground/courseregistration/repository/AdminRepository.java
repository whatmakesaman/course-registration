package studio.thinkground.courseregistration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import studio.thinkground.courseregistration.entity.Admin;
import studio.thinkground.courseregistration.entity.Student;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin,Long>
{
    Admin findByLoginId(String loginId);// 없을 상황 대비
    Boolean existsByLoginId(String loginId);


}
