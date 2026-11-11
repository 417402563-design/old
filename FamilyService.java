package com.antifraud.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.antifraud.common.BusinessException;
import com.antifraud.entity.*;
import com.antifraud.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FamilyService {

    @Autowired
    private FamilyRelationMapper familyRelationMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LearningProgressMapper learningProgressMapper;

    @Autowired
    private AssessmentRecordMapper assessmentRecordMapper;

    @Autowired
    private AlertMapper alertMapper;

    public void bindFamilyMember(Long elderlyId, Long familyMemberId, String relation) {
        // 检查是否已绑定
        LambdaQueryWrapper<FamilyRelation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FamilyRelation::getElderlyId, elderlyId)
               .eq(FamilyRelation::getFamilyMemberId, familyMemberId);
        if (familyRelationMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("该家庭成员已绑定");
        }
        FamilyRelation relation1 = new FamilyRelation();
        relation1.setElderlyId(elderlyId);
        relation1.setFamilyMemberId(familyMemberId);
        relation1.setRelation(relation);
        familyRelationMapper.insert(relation1);
    }

    public List<FamilyRelation> getFamilyMembers(Long elderlyId) {
        LambdaQueryWrapper<FamilyRelation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FamilyRelation::getElderlyId, elderlyId);
        return familyRelationMapper.selectList(wrapper);
    }

    public List<FamilyRelation> getElderlyList(Long familyMemberId) {
        LambdaQueryWrapper<FamilyRelation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FamilyRelation::getFamilyMemberId, familyMemberId);
        return familyRelationMapper.selectList(wrapper);
    }

    public void unbind(Long elderlyId, Long familyMemberId) {
        LambdaQueryWrapper<FamilyRelation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FamilyRelation::getElderlyId, elderlyId)
               .eq(FamilyRelation::getFamilyMemberId, familyMemberId);
        familyRelationMapper.delete(wrapper);
    }

    // 查看老人学习进度
    public List<LearningProgress> getElderlyProgress(Long elderlyId) {
        LambdaQueryWrapper<LearningProgress> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LearningProgress::getUserId, elderlyId);
        return learningProgressMapper.selectList(wrapper);
    }

    // 查看老人评估结果
    public List<AssessmentRecord> getElderlyAssessments(Long elderlyId) {
        LambdaQueryWrapper<AssessmentRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AssessmentRecord::getUserId, elderlyId).orderByDesc(AssessmentRecord::getCreatedAt);
        return assessmentRecordMapper.selectList(wrapper);
    }

    // 发送提醒消息给老人
    public void sendReminder(Long elderlyId, String title, String content) {
        Alert alert = new Alert();
        alert.setUserId(elderlyId);
        alert.setTitle(title);
        alert.setContent(content);
        alert.setAlertType("manual");
        alert.setIsRead(0);
        alertMapper.insert(alert);
    }

    // 通过手机号查找用户
    public User findByPhone(String phone) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, phone);
        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            throw new BusinessException("未找到该手机号对应的用户");
        }
        return user;
    }
}
