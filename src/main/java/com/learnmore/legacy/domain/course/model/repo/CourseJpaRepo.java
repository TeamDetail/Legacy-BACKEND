package com.learnmore.legacy.domain.course.model.repo;

import com.learnmore.legacy.domain.course.model.Course;
import com.learnmore.legacy.domain.ruins.model.Ruins;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseJpaRepo extends JpaRepository<Course, Long> {
    List<Course> findTop10ByOrderByHeartCountDesc();

    List<Course> findTop10ByOrderByCreateAtDesc();

    List<Course> findTop10ByEventIdNotNullOrderByCreateAtDesc();


    @Query("SELECT c FROM Course c WHERE c.courseName LIKE %:name%")
    List<Course> searchByName(@Param("name") String name);
}
