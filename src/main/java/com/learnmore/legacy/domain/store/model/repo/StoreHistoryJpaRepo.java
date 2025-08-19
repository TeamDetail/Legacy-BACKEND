package com.learnmore.legacy.domain.store.model.repo;

import com.learnmore.legacy.domain.store.model.StoreHistory;
import com.learnmore.legacy.domain.user.model.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreHistoryJpaRepo extends JpaRepository<StoreHistory, Long> {
    @Query("SELECT COALESCE(SUM(sh.buyCount), 0) " +
           "FROM StoreHistory sh " +
           "WHERE sh.user = :user " +
           "AND DATE(sh.createAt) = CURRENT_DATE")
    int getTodayBuyCount(@Param("user") User user);
}
