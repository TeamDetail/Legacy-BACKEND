package com.learnmore.legacy.domain.course.model.repo;

import com.learnmore.legacy.domain.course.model.CourseRuins;
import com.learnmore.legacy.domain.ruins.model.Ruins;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRuinsJpaRepo extends JpaRepository<CourseRuins, Long> {
    @Query("select cr.ruins from CourseRuins cr where cr.course.courseId = :courseId order by cr.orderIdx")
    List<Ruins> findRuinsByCourse_CourseId(@Param("courseId") Long courseId);

    Long countByCourse_CourseId(Long courseId);
}
