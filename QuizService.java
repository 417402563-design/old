package com.antifraud.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.antifraud.common.PageResult;
import com.antifraud.dto.QuizSubmitDTO;
import com.antifraud.entity.Quiz;
import com.antifraud.entity.QuizQuestion;
import com.antifraud.entity.QuizRecord;
import com.antifraud.mapper.QuizMapper;
import com.antifraud.mapper.QuizQuestionMapper;
import com.antifraud.mapper.QuizRecordMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuizService {

    @Autowired
    private QuizMapper quizMapper;

    @Autowired
    private QuizQuestionMapper quizQuestionMapper;

    @Autowired
    private QuizRecordMapper quizRecordMapper;

    @Autowired
    private ObjectMapper objectMapper;

    public PageResult<Quiz> listQuizzes(int page, int size) {
        LambdaQueryWrapper<Quiz> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Quiz::getStatus, 1).orderByDesc(Quiz::getCreateTime);
        Page<Quiz> pageResult = quizMapper.selectPage(new Page<>(page, size), wrapper);
        return new PageResult<>(pageResult.getRecords(), pageResult.getTotal(), pageResult.getPages(), pageResult.getCurrent(), pageResult.getSize());
    }

    public Quiz getQuizById(Long id) {
        return quizMapper.selectById(id);
    }

    public List<QuizQuestion> getQuizQuestions(Long quizId) {
        LambdaQueryWrapper<QuizQuestion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(QuizQuestion::getQuizId, quizId).orderByAsc(QuizQuestion::getSortOrder);
        List<QuizQuestion> questions = quizQuestionMapper.selectList(wrapper);
        // 隐藏正确答案和解析
        questions.forEach(q -> {
            q.setCorrectAnswer(null);
            q.setExplanation(null);
        });
        return questions;
    }

    public Map<String, Object> submitQuiz(Long userId, QuizSubmitDTO dto) {
        // 获取所有题目
        LambdaQueryWrapper<QuizQuestion> qWrapper = new LambdaQueryWrapper<>();
        qWrapper.eq(QuizQuestion::getQuizId, dto.getQuizId());
        List<QuizQuestion> questions = quizQuestionMapper.selectList(qWrapper);

        int correctCount = 0;
        List<Map<String, Object>> details = new ArrayList<>();

        for (QuizQuestion question : questions) {
            Map<String, Object> detail = new HashMap<>();
            detail.put("questionId", question.getId());
            detail.put("question", question.getQuestion());
            detail.put("correctAnswer", question.getCorrectAnswer());
            detail.put("explanation", question.getExplanation());

            // 查找用户的答案
            String userAnswer = null;
            for (Map<String, Object> ans : dto.getAnswers()) {
                if (Long.valueOf(ans.get("questionId").toString()).equals(question.getId())) {
                    userAnswer = ans.get("selectedAnswer").toString();
                    break;
                }
            }
            detail.put("userAnswer", userAnswer);
            boolean isCorrect = question.getCorrectAnswer().equals(userAnswer);
            detail.put("isCorrect", isCorrect);
            if (isCorrect) correctCount++;
            details.add(detail);
        }

        int totalQuestions = questions.size();
        int score = totalQuestions > 0 ? (correctCount * 100 / totalQuestions) : 0;

        Quiz quiz = quizMapper.selectById(dto.getQuizId());
        int passed = (quiz != null && score >= quiz.getPassScore()) ? 1 : 0;

        // 保存记录
        try {
            QuizRecord record = new QuizRecord();
            record.setUserId(userId);
            record.setQuizId(dto.getQuizId());
            record.setScore(score);
            record.setPassed(passed);
            record.setTimeSpent(dto.getTimeSpent());
            record.setAnswersJson(objectMapper.writeValueAsString(dto.getAnswers()));
            quizRecordMapper.insert(record);
        } catch (Exception e) {
            // log error
        }

        Map<String, Object> result = new HashMap<>();
        result.put("score", score);
        result.put("passed", passed);
        result.put("totalQuestions", totalQuestions);
        result.put("correctCount", correctCount);
        result.put("details", details);
        return result;
    }

    public List<QuizRecord> getUserRecords(Long userId) {
        LambdaQueryWrapper<QuizRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(QuizRecord::getUserId, userId).orderByDesc(QuizRecord::getCreatedAt);
        return quizRecordMapper.selectList(wrapper);
    }

    // 管理员方法
    public void saveQuiz(Quiz quiz) {
        if (quiz.getId() == null) quizMapper.insert(quiz);
        else quizMapper.updateById(quiz);
    }

    public void deleteQuiz(Long id) { quizMapper.deleteById(id); }

    public void saveQuizQuestion(QuizQuestion q) {
        if (q.getId() == null) quizQuestionMapper.insert(q);
        else quizQuestionMapper.updateById(q);
    }

    public void deleteQuizQuestion(Long id) { quizQuestionMapper.deleteById(id); }
}
