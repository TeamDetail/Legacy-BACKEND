package com.learnmore.legacy.domain.course.service;

import com.learnmore.legacy.domain.course.error.CourseError;
import com.learnmore.legacy.domain.course.model.*;
import com.learnmore.legacy.domain.course.model.repo.*;
import com.learnmore.legacy.domain.course.presentation.dto.request.CourseIdReq;
import com.learnmore.legacy.domain.course.presentation.dto.request.CourseReq;
import com.learnmore.legacy.domain.course.presentation.dto.response.CourseRes;
import com.learnmore.legacy.domain.course.presentation.dto.response.CourseRuinsRes;
import com.learnmore.legacy.domain.ruins.error.RuinsError;
import com.learnmore.legacy.domain.ruins.model.Ruins;
import com.learnmore.legacy.domain.ruins.model.repo.RuinsJpaRepo;
import com.learnmore.legacy.domain.ruins.presentation.dto.response.RuinsDetailRes;
import com.learnmore.legacy.domain.user.model.User;
import com.learnmore.legacy.domain.user.model.repo.UserJpaRepo;
import com.learnmore.legacy.global.exception.CustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseJpaRepo courseJpaRepo;
    private final CourseHeartJpaRepo courseHeartJpaRepo;
    private final CourseTagJpaRepo courseTagJpaRepo;
    private final CourseClearJpaRepo courseClearJpaRepo;
    private final CourseRuinsJpaRepo courseRuinsJpaRepo;
    private final CourseClearHistoryJpaRepo courseClearHistoryJpaRepo;
    private final RuinsJpaRepo ruinsJpaRepo;
    private final UserJpaRepo userJpaRepo;

    public List<CourseRes> getAllCourse(Long userId) {
        return courseJpaRepo.findAll().stream()
                .map(course -> {
                    Long courseId = course.getCourseId();

                    List<String> tagNames =
                            courseTagJpaRepo.findTagNamesByCourse_CourseId(courseId);

                    List<Long> ruinsIds =
                            courseRuinsJpaRepo.findRuinsByCourse_CourseId(courseId)
                                    .stream()
                                    .map(Ruins::getRuinsId)
                                    .toList();

                    boolean isClear =
                            courseClearHistoryJpaRepo.existsByCourseClear_Course_CourseIdAndUser_UserId(courseId, userId);

                    Long clearRuinsCount = courseClearHistoryJpaRepo.countClearedRuinsByCourseAndUser(courseId, userId);

                    boolean isHeart = courseHeartJpaRepo.existsByCourseAndUser_UserId(course, userId);
                    Ruins ruins = ruinsJpaRepo.findById(ruinsIds.getFirst())
                            .orElseThrow(() -> new CustomException(RuinsError.RUINS_NOT_FOUND));

                    String thumbnail = ruins.getRuinsImage();

                    Long maxRuinsCount = courseRuinsJpaRepo.countByCourse_CourseId(courseId);


                    return CourseRes.from(
                            course,
                            tagNames,
                            isClear,
                            isHeart,
                            thumbnail,
                            clearRuinsCount,
                            maxRuinsCount
                    );
                })
                .collect(Collectors.toList());
    }

    public List<CourseRes> getAllPopularCourse(Long userId) {
        return courseJpaRepo.findTop10ByOrderByHeartCountDesc().stream()
                .map(course -> {
                    Long courseId = course.getCourseId();

                    List<String> tagNames =
                            courseTagJpaRepo.findTagNamesByCourse_CourseId(courseId);

                    List<Long> ruinsIds =
                            courseRuinsJpaRepo.findRuinsByCourse_CourseId(courseId)
                                    .stream()
                                    .map(Ruins::getRuinsId)
                                    .toList();

                    boolean isClear =
                            courseClearHistoryJpaRepo.existsByCourseClear_Course_CourseIdAndUser_UserId(courseId, userId);

                    Long clearRuinsCount = courseClearHistoryJpaRepo.countClearedRuinsByCourseAndUser(courseId, userId);

                    boolean isHeart = courseHeartJpaRepo.existsByCourseAndUser_UserId(course, userId);
                    Ruins ruins = ruinsJpaRepo.findById(ruinsIds.getFirst())
                            .orElseThrow(() -> new CustomException(RuinsError.RUINS_NOT_FOUND));

                    String thumbnail = ruins.getRuinsImage();

                    Long maxRuinsCount = courseRuinsJpaRepo.countByCourse_CourseId(courseId);

                    return CourseRes.from(
                            course,
                            tagNames,
                            isClear,
                            isHeart,
                            thumbnail,
                            clearRuinsCount,
                            maxRuinsCount
                    );
                })
                .collect(Collectors.toList());
    }

    public List<CourseRes> getAllRecentCourse(Long userId) {
        return courseJpaRepo.findTop10ByOrderByCreateAtDesc().stream()
                .map(course -> {
                    Long courseId = course.getCourseId();

                    List<String> tagNames =
                            courseTagJpaRepo.findTagNamesByCourse_CourseId(courseId);

                    List<Long> ruinsIds =
                            courseRuinsJpaRepo.findRuinsByCourse_CourseId(courseId)
                                    .stream()
                                    .map(Ruins::getRuinsId)
                                    .toList();

                    boolean isClear =
                            courseClearHistoryJpaRepo.existsByCourseClear_Course_CourseIdAndUser_UserId(courseId, userId);

                    Long clearRuinsCount = courseClearHistoryJpaRepo.countClearedRuinsByCourseAndUser(courseId, userId);

                    boolean isHeart = courseHeartJpaRepo.existsByCourseAndUser_UserId(course, userId);

                    Ruins ruins = ruinsJpaRepo.findById(ruinsIds.getFirst())
                            .orElseThrow(() -> new CustomException(RuinsError.RUINS_NOT_FOUND));

                    String thumbnail = ruins.getRuinsImage();

                    Long maxRuinsCount = courseRuinsJpaRepo.countByCourse_CourseId(courseId);

                    return CourseRes.from(
                            course,
                            tagNames,
                            isClear,
                            isHeart,
                            thumbnail,
                            clearRuinsCount,
                            maxRuinsCount
                    );
                })
                .collect(Collectors.toList());
    }

    public List<CourseRes> getAllEventCourse(Long userId) {
        return courseJpaRepo.findTop10ByEventIdNotNullOrderByCreateAtDesc().stream()
                .map(course -> {
                    Long courseId = course.getCourseId();

                    List<String> tagNames = courseTagJpaRepo.findTagNamesByCourse_CourseId(courseId);

                    List<Long> ruinsIds = courseRuinsJpaRepo.findRuinsByCourse_CourseId(courseId)
                            .stream()
                            .map(Ruins::getRuinsId)
                            .toList();

                    boolean isClear = courseClearHistoryJpaRepo.existsByCourseClear_Course_CourseIdAndUser_UserId(courseId, userId);

                    Long clearRuinsCount = courseClearHistoryJpaRepo.countClearedRuinsByCourseAndUser(courseId, userId);

                    boolean isHeart = courseHeartJpaRepo.existsByCourseAndUser_UserId(course, userId);
                    Ruins ruins = ruinsJpaRepo.findById(ruinsIds.getFirst())
                            .orElseThrow(() -> new CustomException(RuinsError.RUINS_NOT_FOUND));

                    String thumbnail = ruins.getRuinsImage();

                    Long maxRuinsCount = courseRuinsJpaRepo.countByCourse_CourseId(courseId);

                    return CourseRes.from(course, tagNames,  isClear,  isHeart, thumbnail, clearRuinsCount, maxRuinsCount);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void toggleHeart(CourseIdReq courseIdReq, Long userId) {
        Long courseId = courseIdReq.getCourseId();
        User user = userJpaRepo.findByUserId(userId);

        Course course = courseJpaRepo.findById(courseId)
                .orElseThrow(() -> new CustomException(CourseError.COURSE_ERROR));

        Optional<CourseHeart> existingHeart = courseHeartJpaRepo.findByCourseAndUser(course, user);

        if (existingHeart.isPresent()) {
            // 좋아요 취소
            courseHeartJpaRepo.delete(existingHeart.get());
            course.setHeartCount(course.getHeartCount() - 1);
        } else {
            // 좋아요 등록
            CourseHeart newHeart = CourseHeart.builder()
                    .course(course)
                    .user(user)
                    .build();
            courseHeartJpaRepo.save(newHeart);
            course.setHeartCount(course.getHeartCount() + 1);
        }
        courseJpaRepo.save(course);
    }

    @Transactional
    public CourseRes addCourse(CourseReq courseReq, Long userId) {
        User user = userJpaRepo.findByUserId(userId);

        Course course = Course.builder()
                .courseName(courseReq.getName())
                .courseDescription(courseReq.getDescription())
                .eventId(null)
                .heartCount(0)
                .clearCount(0)
                .user(user)
                .build();

        courseJpaRepo.save(course);

        List<CourseTag> tags = courseReq.getTag().stream()
                .map(tagName -> CourseTag.builder()
                        .tagName(tagName)
                        .course(course)
                        .build())
                .collect(Collectors.toList());
        courseTagJpaRepo.saveAll(tags);


        List<CourseRuins> courseRuinsList = new ArrayList<>();
        int orderIdx = 1;
        for (Long ruinsId : courseReq.getRuinsId()) {
            Ruins ruins = ruinsJpaRepo.findById(ruinsId)
                    .orElseThrow(() -> new CustomException(RuinsError.RUINS_NOT_FOUND));

            CourseRuins courseRuins = CourseRuins.builder()
                    .course(course)
                    .ruins(ruins)
                    .orderIdx(orderIdx++)
                    .build();
            courseRuinsList.add(courseRuins);
        }
        courseRuinsJpaRepo.saveAll(courseRuinsList);

        Ruins ruins = ruinsJpaRepo.findById(courseReq.getRuinsId().getFirst())
                .orElseThrow(() -> new CustomException(RuinsError.RUINS_NOT_FOUND));

        String thumbnail = ruins.getRuinsImage();

        return CourseRes.from(course, courseReq.getTag(), false,  false, thumbnail, 0L, 0L);
    }

    public CourseRuinsRes getRuinsAndClearRuins(Long courseId, Long userId) {
        List<Ruins> ruinsList = courseRuinsJpaRepo.findRuinsByCourse_CourseId(courseId);

        List<Ruins> clearRuinsList = courseClearHistoryJpaRepo.findRuinsByCourseIdAndUserId(courseId, userId);

        List<RuinsDetailRes> ruinsResList = ruinsList.stream()
                .map(r -> RuinsDetailRes.from(r, null))
                .collect(Collectors.toList());

        List<RuinsDetailRes> clearRuinsResList = clearRuinsList.stream()
                .map(r -> RuinsDetailRes.from(r, null))
                .collect(Collectors.toList());

        return CourseRuinsRes.builder()
                .ruins(ruinsResList)
                .clearRuins(clearRuinsResList)
                .build();
    }
}
