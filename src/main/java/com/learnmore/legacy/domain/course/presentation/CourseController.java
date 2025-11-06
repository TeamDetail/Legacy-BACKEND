package com.learnmore.legacy.domain.course.presentation;

import com.learnmore.legacy.domain.course.presentation.dto.request.CourseIdReq;
import com.learnmore.legacy.domain.course.presentation.dto.request.CourseReq;
import com.learnmore.legacy.domain.course.presentation.dto.response.CourseRes;
import com.learnmore.legacy.domain.course.presentation.dto.response.CourseRuinsRes;
import com.learnmore.legacy.domain.course.service.CourseService;
import com.learnmore.legacy.global.common.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "코스", description = "코스 API")
@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @Operation(summary = "코스 모두 조회", description = "코스를 모두 조회합니다.")
    @GetMapping
    public ResponseEntity<BaseResponse<List<CourseRes>>> getCourse() {
        return BaseResponse.of(courseService.getAllCourse());
    }

    @Operation(summary = "인기 있는 코스 모두 조회", description = "최근 3달 이내에 생성된 코스 중, 좋아요 높은 순서대로 10개를 조회합니다.")
    @GetMapping("/popular")
    public ResponseEntity<BaseResponse<List<CourseRes>>> getPopularCourses() {
        return BaseResponse.of(courseService.getAllPopularCourse());
    }

    @Operation(summary = "최근 제작된 코스 모두 조회", description = "최신순으로 제작된 코스 10개를 조회합니다.")
    @GetMapping("/recent")
    public ResponseEntity<BaseResponse<List<CourseRes>>> getRecentCourses() {
        return BaseResponse.of(courseService.getAllRecentCourse());
    }

    @Operation(summary = "이벤트 코스 모두 조회", description = "이벤트 코스를 10개 조회합니다.")
    @GetMapping("/event")
    public ResponseEntity<BaseResponse<List<CourseRes>>> getEventCourses() {
        return BaseResponse.of(courseService.getAllEventCourse());
    }

    @Operation(summary = "코스 좋아요 토글", description = "코스 좋아요를 취소하거나 등록합니다.")
    @PatchMapping
    public ResponseEntity<BaseResponse<String>> toggleHeart(@RequestBody CourseIdReq courseIdReq) {
        courseService.toggleHeart(courseIdReq);
        return BaseResponse.of("ok");
    }

    @Operation(summary = "코스 등록", description = "코스를 등록합니다.")
    @PostMapping
    public ResponseEntity<BaseResponse<CourseRes>> createCourse(@RequestBody CourseReq courseReq) {
        return BaseResponse.of(courseService.addCourse(courseReq));
    }

    @Operation(summary = "코스 상세 조회", description = "코스를 자세히 봅니다.")
    @GetMapping("/{courseId}")
    public ResponseEntity<BaseResponse<CourseRuinsRes>> getCourse(@PathVariable Long courseId) {
        return BaseResponse.of(courseService.getRuinsAndClearRuins(courseId));
    }

    @Operation(summary = "코스 이름으로 검색", description = "코스 이름으로 검색합니다.")
    @GetMapping("/search")
    public ResponseEntity<BaseResponse<List<CourseRes>>> getRuinsDetailByName(@RequestParam String courseName) {
        return BaseResponse.of(courseService.getCourseByCourseName(courseName));
    }

}
