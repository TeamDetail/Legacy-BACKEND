package com.learnmore.legacy.domain.daily.model.repo;

import com.learnmore.legacy.domain.daily.model.DailyCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DailyCheckJpaRepo extends JpaRepository<DailyCheck, Long> {
    List<DailyCheck> findByIsActivateTrueAndStartAtLessThanEqualAndEndAtGreaterThanEqual(LocalDate today, LocalDate today1);
}
