package com.learnmore.legacy.domain.course.model.repo;

import com.learnmore.legacy.domain.course.model.CourseClear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseClearJpaRepo extends JpaRepository<CourseClear, Long> {
    boolean existsByCourse_CourseIdAndUser_UserId(Long courseId, Long userId);
}
