package com.learnmore.legacy.domain.daily.model.repo;

import com.learnmore.legacy.domain.daily.model.DailyCheckHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DailyCheckHistoryJpaRepo extends JpaRepository<DailyCheckHistory, Long> {
    boolean existsByUser_UserIdAndDailyCheck_DailyCheckIdAndCheckDate(
            Long userId,
            Long dailyCheckId,
            LocalDate checkDate
    );

    Optional<DailyCheckHistory> findByUser_UserIdAndDailyCheck_DailyCheckId(Long userId, Long dailyCheckId);

    Optional<DailyCheckHistory> findByUser_UserIdAndDailyCheck_DailyCheckIdAndCheckDate(Long userId, Long dailyCheckId, LocalDate today);
}
