package com.learnmore.legacy.domain.course.model.repo;

import com.learnmore.legacy.domain.course.model.CourseClearHistory;
import com.learnmore.legacy.domain.ruins.model.Ruins;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseClearHistoryJpaRepo extends JpaRepository<CourseClearHistory, Long> {
    @Query("""
    select cch.ruins
    from CourseClearHistory cch
    where cch.courseClear.course.courseId = :courseId
      and cch.courseClear.user.userId = :userId
""")
    List<Ruins> findRuinsByCourseIdAndUserId(@Param("courseId") Long courseId,
                                             @Param("userId") Long userId);
}
