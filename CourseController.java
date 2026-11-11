package com.antifraud.controller;

import com.antifraud.common.PageResult;
import com.antifraud.common.Result;
import com.antifraud.entity.Chapter;
import com.antifraud.entity.Course;
import com.antifraud.entity.LearningProgress;
import com.antifraud.service.CourseService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/list")
    public Result<PageResult<Course>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer difficulty) {
        return Result.success(courseService.listCourses(page, size, category, difficulty));
    }

    @GetMapping("/{id}")
    public Result<Course> getDetail(@PathVariable Long id) {
        return Result.success(courseService.getCourseById(id));
    }

    @GetMapping("/{courseId}/chapters")
    public Result<List<Chapter>> getChapters(@PathVariable Long courseId) {
        return Result.success(courseService.getChaptersByCourseId(courseId));
    }

    @GetMapping("/chapter/{chapterId}")
    public Result<Chapter> getChapter(@PathVariable Long chapterId) {
        return Result.success(courseService.getChapterById(chapterId));
    }

    @PostMapping("/progress")
    public Result<?> updateProgress(HttpServletRequest request, @RequestBody Map<String, Object> body) {
        Long userId = (Long) request.getAttribute("userId");
        Long courseId = Long.valueOf(body.get("courseId").toString());
        Long chapterId = Long.valueOf(body.get("chapterId").toString());
        Integer progressPercent = Integer.valueOf(body.get("progressPercent").toString());
        courseService.updateProgress(userId, courseId, chapterId, progressPercent);
        return Result.success("进度已保存", null);
    }

    @GetMapping("/progress")
    public Result<List<LearningProgress>> getProgress(
            HttpServletRequest request,
            @RequestParam(required = false) Long courseId) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(courseService.getUserProgress(userId, courseId));
    }
}
