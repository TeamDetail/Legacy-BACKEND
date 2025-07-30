package com.learnmore.legacy.domain.course.model.repo;

import com.learnmore.legacy.domain.course.model.CourseTag;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseTagJpaRepo extends JpaRepository<CourseTag, Long> {
    @Query("select ct.tagName from CourseTag ct where ct.course.courseId = :courseId")
    List<String> findTagNamesByCourse_CourseId(@Param("courseId") Long courseId);
}
