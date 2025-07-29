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
        SELECT COUNT(cch)
        FROM CourseClearHistory cch
        WHERE cch.user.userId = :userId
        AND cch.ruins.ruinsId IN (
            SELECT cr.ruins.ruinsId FROM CourseRuins cr WHERE cr.course.courseId = :courseId
        )
    """)
    Long countClearedRuinsByCourseAndUser(@Param("courseId") Long courseId, @Param("userId") Long userId);

    boolean existsByCourseClear_Course_CourseIdAndUser_UserId(Long courseId, Long userId);

    @Query("select cch.ruins from CourseClearHistory cch where cch.courseClear.course.courseId = :courseId and cch.user.userId = :userId")
    List<Ruins> findRuinsByCourseIdAndUserId(@Param("courseId") Long courseId, @Param("userId") Long userId);

}
