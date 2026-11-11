package com.antifraud.controller;

import com.antifraud.common.Result;
import com.antifraud.dto.AssessmentSubmitDTO;
import com.antifraud.entity.Alert;
import com.antifraud.entity.AssessmentQuestion;
import com.antifraud.entity.AssessmentRecord;
import com.antifraud.service.AssessmentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assessment")
public class AssessmentController {

    @Autowired
    private AssessmentService assessmentService;

    @GetMapping("/questions")
    public Result<List<AssessmentQuestion>> getQuestions() {
        return Result.success(assessmentService.getQuestions());
    }

    @PostMapping("/submit")
    public Result<AssessmentRecord> submit(HttpServletRequest request, @RequestBody AssessmentSubmitDTO dto) {
        Long userId = (Long) request.getAttribute("userId");
        AssessmentRecord record = assessmentService.submitAssessment(userId, dto);
        return Result.success("评估完成", record);
    }

    @GetMapping("/records")
    public Result<List<AssessmentRecord>> getRecords(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(assessmentService.getUserRecords(userId));
    }

    // 预警相关
    @GetMapping("/alerts")
    public Result<List<Alert>> getAlerts(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(assessmentService.getUserAlerts(userId));
    }

    @GetMapping("/alerts/unread")
    public Result<List<Alert>> getUnreadAlerts(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(assessmentService.getUnreadAlerts(userId));
    }

    @PutMapping("/alerts/{alertId}/read")
    public Result<?> markRead(@PathVariable Long alertId) {
        assessmentService.markAlertRead(alertId);
        return Result.success("已标记为已读", null);
    }
}
