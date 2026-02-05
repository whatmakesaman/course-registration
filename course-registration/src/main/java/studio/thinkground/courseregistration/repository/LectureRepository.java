package studio.thinkground.courseregistration.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import studio.thinkground.courseregistration.entity.Lecture;

import java.util.Optional;


public interface LectureRepository extends JpaRepository<Lecture,Long> {

    //비관적 락 적용
    //이 메서드로 조회하는 순간,트랙잭션이 끝날 때까지 다른 누구도 이 데이터 건들 수 없음
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select l from Lecture l where l.id=:id")
    Optional<Lecture> findByIdWithLock(@Param("id") Long id);
}
