package com.antifraud.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.antifraud.common.Constants;
import com.antifraud.dto.AssessmentSubmitDTO;
import com.antifraud.entity.AssessmentQuestion;
import com.antifraud.entity.AssessmentRecord;
import com.antifraud.entity.Alert;
import com.antifraud.mapper.AssessmentQuestionMapper;
import com.antifraud.mapper.AssessmentRecordMapper;
import com.antifraud.mapper.AlertMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AssessmentService {

    @Autowired
    private AssessmentQuestionMapper assessmentQuestionMapper;

    @Autowired
    private AssessmentRecordMapper assessmentRecordMapper;

    @Autowired
    private AlertMapper alertMapper;

    public List<AssessmentQuestion> getQuestions() {
        LambdaQueryWrapper<AssessmentQuestion> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(AssessmentQuestion::getId);
        return assessmentQuestionMapper.selectList(wrapper);
    }

    public AssessmentRecord submitAssessment(Long userId, AssessmentSubmitDTO dto) {
        int totalScore = 0;
        for (Map<String, Object> answer : dto.getAnswers()) {
            Long questionId = Long.valueOf(answer.get("questionId").toString());
            Integer selectedValue = Integer.valueOf(answer.get("selectedValue").toString());
            totalScore += selectedValue;
        }

        String riskLevel;
        String suggestions;
        if (totalScore <= 5) {
            riskLevel = Constants.RISK_LOW;
            suggestions = "您的防诈骗意识较好，请继续保持警惕。建议定期复习反诈知识，关注最新诈骗手法。";
        } else if (totalScore <= 12) {
            riskLevel = Constants.RISK_MEDIUM;
            suggestions = "您的防诈骗意识中等，建议加强以下方面：\n1. 不轻信陌生来电和短信\n2. 不点击不明链接\n3. 遇到可疑情况及时联系家人或报警";
        } else {
            riskLevel = Constants.RISK_HIGH;
            suggestions = "您的防诈骗意识需要加强，建议：\n1. 系统学习平台反诈课程\n2. 安装国家反诈中心APP\n3. 设置转账限额\n4. 遇到可疑情况拨打110或96110\n5. 告知家人共同防范";
            // 高风险用户推送预警
            Alert alert = new Alert();
            alert.setUserId(userId);
            alert.setTitle("风险提示");
            alert.setContent("根据您的评估结果，您属于诈骗高风险人群，请务必学习防范知识并告知家人。");
            alert.setAlertType("risk");
            alert.setIsRead(0);
            alertMapper.insert(alert);
        }

        AssessmentRecord record = new AssessmentRecord();
        record.setUserId(userId);
        record.setScore(totalScore);
        record.setRiskLevel(riskLevel);
        record.setSuggestions(suggestions);
        assessmentRecordMapper.insert(record);

        return record;
    }

    public List<AssessmentRecord> getUserRecords(Long userId) {
        LambdaQueryWrapper<AssessmentRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AssessmentRecord::getUserId, userId).orderByDesc(AssessmentRecord::getCreatedAt);
        return assessmentRecordMapper.selectList(wrapper);
    }

    // 预警管理
    public List<Alert> getUserAlerts(Long userId) {
        LambdaQueryWrapper<Alert> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Alert::getUserId, userId).orderByDesc(Alert::getCreatedAt);
        return alertMapper.selectList(wrapper);
    }

    public List<Alert> getUnreadAlerts(Long userId) {
        LambdaQueryWrapper<Alert> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Alert::getUserId, userId).eq(Alert::getIsRead, 0).orderByDesc(Alert::getCreatedAt);
        return alertMapper.selectList(wrapper);
    }

    public void markAlertRead(Long alertId) {
        Alert alert = alertMapper.selectById(alertId);
        if (alert != null) {
            alert.setIsRead(1);
            alertMapper.updateById(alert);
        }
    }

    public void createAlert(Alert alert) {
        alert.setIsRead(0);
        alertMapper.insert(alert);
    }
}
