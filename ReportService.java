package com.antifraud.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.antifraud.entity.*;
import com.antifraud.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    @Autowired
    private LearningProgressMapper learningProgressMapper;

    @Autowired
    private AssessmentRecordMapper assessmentRecordMapper;

    @Autowired
    private QuizRecordMapper quizRecordMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CourseMapper courseMapper;

    /**
     * 个人学习报告
     */
    public Map<String, Object> getPersonalReport(Long userId) {
        Map<String, Object> report = new HashMap<>();

        // 学习进度统计
        LambdaQueryWrapper<LearningProgress> lpWrapper = new LambdaQueryWrapper<>();
        lpWrapper.eq(LearningProgress::getUserId, userId);
        List<LearningProgress> progressList = learningProgressMapper.selectList(lpWrapper);
        long completedChapters = progressList.stream().filter(p -> p.getCompleted() == 1).count();
        report.put("totalChaptersLearned", progressList.size());
        report.put("completedChapters", completedChapters);

        // 评估记录
        LambdaQueryWrapper<AssessmentRecord> arWrapper = new LambdaQueryWrapper<>();
        arWrapper.eq(AssessmentRecord::getUserId, userId).orderByDesc(AssessmentRecord::getCreatedAt);
        List<AssessmentRecord> assessments = assessmentRecordMapper.selectList(arWrapper);
        report.put("assessmentCount", assessments.size());
        if (!assessments.isEmpty()) {
            report.put("latestRiskLevel", assessments.get(0).getRiskLevel());
            report.put("latestScore", assessments.get(0).getScore());
        }

        // 测试成绩
        LambdaQueryWrapper<QuizRecord> qrWrapper = new LambdaQueryWrapper<>();
        qrWrapper.eq(QuizRecord::getUserId, userId);
        List<QuizRecord> quizRecords = quizRecordMapper.selectList(qrWrapper);
        report.put("quizCount", quizRecords.size());
        long passedQuizzes = quizRecords.stream().filter(q -> q.getPassed() == 1).count();
        report.put("passedQuizzes", passedQuizzes);
        if (!quizRecords.isEmpty()) {
            double avgScore = quizRecords.stream().mapToInt(QuizRecord::getScore).average().orElse(0);
            report.put("averageQuizScore", Math.round(avgScore));
        }

        return report;
    }

    /**
     * 平台整体统计报告（管理员用）
     */
    public Map<String, Object> getPlatformReport() {
        Map<String, Object> report = new HashMap<>();

        // 用户总数
        report.put("totalUsers", userMapper.selectCount(null));

        // 课程总数
        report.put("totalCourses", courseMapper.selectCount(null));

        // 总学习记录
        report.put("totalLearningRecords", learningProgressMapper.selectCount(null));

        // 评估统计
        List<AssessmentRecord> allAssessments = assessmentRecordMapper.selectList(null);
        report.put("totalAssessments", allAssessments.size());
        long highRisk = allAssessments.stream().filter(a -> "high".equals(a.getRiskLevel())).count();
        long mediumRisk = allAssessments.stream().filter(a -> "medium".equals(a.getRiskLevel())).count();
        long lowRisk = allAssessments.stream().filter(a -> "low".equals(a.getRiskLevel())).count();
        Map<String, Long> riskDistribution = new HashMap<>();
        riskDistribution.put("high", highRisk);
        riskDistribution.put("medium", mediumRisk);
        riskDistribution.put("low", lowRisk);
        report.put("riskDistribution", riskDistribution);

        // 测试统计
        List<QuizRecord> allQuizRecords = quizRecordMapper.selectList(null);
        report.put("totalQuizRecords", allQuizRecords.size());
        long passed = allQuizRecords.stream().filter(q -> q.getPassed() == 1).count();
        report.put("quizPassRate", allQuizRecords.isEmpty() ? 0 : Math.round(passed * 100.0 / allQuizRecords.size()));

        return report;
    }
}
