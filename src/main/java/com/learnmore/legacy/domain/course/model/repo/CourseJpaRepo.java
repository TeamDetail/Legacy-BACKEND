package com.learnmore.legacy.domain.course.model.repo;

import com.learnmore.legacy.domain.course.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseJpaRepo extends JpaRepository<Course, Long> {
    List<Course> findTop10ByOrderByHeartCountDesc();

    List<Course> findTop10ByOrderByCreateAtDesc();

    List<Course> findTop10ByEventIdNotNullOrderByCreateAtDesc();
}
