package com.antifraud.controller;

import com.antifraud.common.Result;
import com.antifraud.entity.AssessmentRecord;
import com.antifraud.entity.FamilyRelation;
import com.antifraud.entity.LearningProgress;
import com.antifraud.entity.User;
import com.antifraud.service.FamilyService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/family")
public class FamilyController {

    @Autowired
    private FamilyService familyService;

    @PostMapping("/bind")
    public Result<?> bind(HttpServletRequest request, @RequestBody Map<String, Object> body) {
        Long currentUserId = (Long) request.getAttribute("userId");
        String phone = body.get("phone").toString();
        String relation = body.get("relation").toString();

        User elderly = familyService.findByPhone(phone);
        familyService.bindFamilyMember(elderly.getId(), currentUserId, relation);
        return Result.success("绑定成功", null);
    }

    @GetMapping("/members")
    public Result<List<FamilyRelation>> getMembers(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        String role = (String) request.getAttribute("role");
        if ("elderly".equals(role)) {
            return Result.success(familyService.getFamilyMembers(userId));
        } else {
            return Result.success(familyService.getElderlyList(userId));
        }
    }

    @DeleteMapping("/unbind")
    public Result<?> unbind(HttpServletRequest request, @RequestBody Map<String, Long> body) {
        familyService.unbind(body.get("elderlyId"), body.get("familyMemberId"));
        return Result.success("已解绑", null);
    }

    @GetMapping("/elderly/{elderlyId}/progress")
    public Result<List<LearningProgress>> getElderlyProgress(@PathVariable Long elderlyId) {
        return Result.success(familyService.getElderlyProgress(elderlyId));
    }

    @GetMapping("/elderly/{elderlyId}/assessments")
    public Result<List<AssessmentRecord>> getElderlyAssessments(@PathVariable Long elderlyId) {
        return Result.success(familyService.getElderlyAssessments(elderlyId));
    }

    @PostMapping("/remind")
    public Result<?> sendReminder(@RequestBody Map<String, String> body) {
        Long elderlyId = Long.valueOf(body.get("elderlyId"));
        familyService.sendReminder(elderlyId, body.get("title"), body.get("content"));
        return Result.success("提醒已发送", null);
    }
}
