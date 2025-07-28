package com.learnmore.legacy.domain.course.model.repo;

import com.learnmore.legacy.domain.course.model.Course;
import com.learnmore.legacy.domain.course.model.CourseHeart;
import com.learnmore.legacy.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseHeartJpaRepo extends JpaRepository<CourseHeart, Long> {
    Optional<CourseHeart> findByCourseAndUser(Course course, User user);

    boolean existsByCourseAndUser(Course course, User user);
}
