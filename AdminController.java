package com.antifraud.controller;

import com.antifraud.common.PageResult;
import com.antifraud.common.Result;
import com.antifraud.dto.LoginDTO;
import com.antifraud.dto.LoginResultDTO;
import com.antifraud.entity.*;
import com.antifraud.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private FraudCaseService fraudCaseService;

    @Autowired
    private QuizService quizService;

    @Autowired
    private AssessmentService assessmentService;

    // 管理员登录
    @PostMapping("/auth/login")
    public Result<LoginResultDTO> login(@Valid @RequestBody LoginDTO dto) {
        return Result.success("登录成功", adminService.adminLogin(dto));
    }

    // 用户管理
    @GetMapping("/users")
    public Result<PageResult<User>> listUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String role) {
        return Result.success(adminService.listUsers(page, size, role));
    }

    @PutMapping("/users/{userId}/toggle")
    public Result<?> toggleUser(@PathVariable Long userId, HttpServletRequest request) {
        adminService.toggleUserStatus(userId);
        adminService.logOperation((Long) request.getAttribute("userId"), "禁用/启用用户", "user:" + userId, "");
        return Result.success("操作成功", null);
    }

    // 课程管理
    @PostMapping("/courses")
    public Result<?> saveCourse(@RequestBody Course course, HttpServletRequest request) {
        courseService.saveCourse(course);
        adminService.logOperation((Long) request.getAttribute("userId"), "保存课程", "course:" + course.getId(), course.getTitle());
        return Result.success("保存成功", null);
    }

    @DeleteMapping("/courses/{id}")
    public Result<?> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return Result.success("删除成功", null);
    }

    @PostMapping("/chapters")
    public Result<?> saveChapter(@RequestBody Chapter chapter) {
        courseService.saveChapter(chapter);
        return Result.success("保存成功", null);
    }

    @DeleteMapping("/chapters/{id}")
    public Result<?> deleteChapter(@PathVariable Long id) {
        courseService.deleteChapter(id);
        return Result.success("删除成功", null);
    }

    // 案例管理
    @PostMapping("/cases")
    public Result<?> saveCase(@RequestBody FraudCase fraudCase) {
        fraudCaseService.saveCase(fraudCase);
        return Result.success("保存成功", null);
    }

    @DeleteMapping("/cases/{id}")
    public Result<?> deleteCase(@PathVariable Long id) {
        fraudCaseService.deleteCase(id);
        return Result.success("删除成功", null);
    }

    // 测试管理
    @PostMapping("/quizzes")
    public Result<?> saveQuiz(@RequestBody Quiz quiz) {
        quizService.saveQuiz(quiz);
        return Result.success("保存成功", null);
    }

    @DeleteMapping("/quizzes/{id}")
    public Result<?> deleteQuiz(@PathVariable Long id) {
        quizService.deleteQuiz(id);
        return Result.success("删除成功", null);
    }

    @PostMapping("/quiz-questions")
    public Result<?> saveQuizQuestion(@RequestBody QuizQuestion question) {
        quizService.saveQuizQuestion(question);
        return Result.success("保存成功", null);
    }

    @DeleteMapping("/quiz-questions/{id}")
    public Result<?> deleteQuizQuestion(@PathVariable Long id) {
        quizService.deleteQuizQuestion(id);
        return Result.success("删除成功", null);
    }

    // 预警管理
    @PostMapping("/alerts")
    public Result<?> createAlert(@RequestBody Alert alert) {
        assessmentService.createAlert(alert);
        return Result.success("预警已发布", null);
    }

    // 操作日志
    @GetMapping("/logs")
    public Result<PageResult<OperationLog>> getLogs(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Long adminId) {
        return Result.success(adminService.getOperationLogs(page, size, adminId));
    }
}
