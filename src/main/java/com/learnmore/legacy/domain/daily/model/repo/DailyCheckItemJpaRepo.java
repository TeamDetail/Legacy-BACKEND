package com.learnmore.legacy.domain.daily.model.repo;

import com.learnmore.legacy.domain.daily.model.DailyCheck;
import com.learnmore.legacy.domain.daily.model.DailyCheckItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DailyCheckItemJpaRepo extends JpaRepository<DailyCheckItem, Long> {
    List<DailyCheckItem> findByDailyCheckAndDayNumber(DailyCheck event, int dayNumber);

    List<DailyCheckItem> findByDailyCheckOrderByDayNumber(DailyCheck dailyCheck);
}
