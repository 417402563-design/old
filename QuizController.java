package com.antifraud.controller;

import com.antifraud.common.PageResult;
import com.antifraud.common.Result;
import com.antifraud.dto.QuizSubmitDTO;
import com.antifraud.entity.Quiz;
import com.antifraud.entity.QuizQuestion;
import com.antifraud.entity.QuizRecord;
import com.antifraud.service.QuizService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @GetMapping("/list")
    public Result<PageResult<Quiz>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(quizService.listQuizzes(page, size));
    }

    @GetMapping("/{id}")
    public Result<Quiz> getDetail(@PathVariable Long id) {
        return Result.success(quizService.getQuizById(id));
    }

    @GetMapping("/{quizId}/questions")
    public Result<List<QuizQuestion>> getQuestions(@PathVariable Long quizId) {
        return Result.success(quizService.getQuizQuestions(quizId));
    }

    @PostMapping("/submit")
    public Result<Map<String, Object>> submit(HttpServletRequest request, @RequestBody QuizSubmitDTO dto) {
        Long userId = (Long) request.getAttribute("userId");
        Map<String, Object> result = quizService.submitQuiz(userId, dto);
        return Result.success("测试完成", result);
    }

    @GetMapping("/records")
    public Result<List<QuizRecord>> getRecords(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(quizService.getUserRecords(userId));
    }
}
