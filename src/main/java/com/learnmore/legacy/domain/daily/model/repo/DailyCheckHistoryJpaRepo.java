package com.learnmore.legacy.domain.daily.model.repo;

import com.learnmore.legacy.domain.daily.model.DailyCheck;
import com.learnmore.legacy.domain.daily.model.DailyCheckHistory;
import com.learnmore.legacy.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Repository
public interface DailyCheckHistoryJpaRepo extends JpaRepository<DailyCheckHistory, Long> {
    boolean existsByUserAndDailyCheckAndCheckDate(User user, DailyCheck event, LocalDate today);

    Optional<DailyCheckHistory> findFirstByUserAndDailyCheckOrderByDayNumberDesc(User user, DailyCheck dailyCheck);

    Optional<DailyCheckHistory> findByUserAndDailyCheckAndCheckDate(User user, DailyCheck event, LocalDate today);

    Optional<DailyCheckHistory> findByUserAndDailyCheck(User user, DailyCheck event);
}
