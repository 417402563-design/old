package com.antifraud.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.antifraud.common.PageResult;
import com.antifraud.entity.*;
import com.antifraud.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private ChapterMapper chapterMapper;

    @Autowired
    private LearningProgressMapper learningProgressMapper;

    public PageResult<Course> listCourses(int page, int size, String category, Integer difficulty) {
        LambdaQueryWrapper<Course> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Course::getStatus, 1);
        if (category != null && !category.isEmpty()) {
            wrapper.eq(Course::getCategory, category);
        }
        if (difficulty != null) {
            wrapper.eq(Course::getDifficulty, difficulty);
        }
        wrapper.orderByAsc(Course::getSortOrder);
        Page<Course> pageResult = courseMapper.selectPage(new Page<>(page, size), wrapper);
        return new PageResult<>(pageResult.getRecords(), pageResult.getTotal(), pageResult.getPages(), pageResult.getCurrent(), pageResult.getSize());
    }

    public Course getCourseById(Long id) {
        return courseMapper.selectById(id);
    }

    public List<Chapter> getChaptersByCourseId(Long courseId) {
        LambdaQueryWrapper<Chapter> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Chapter::getCourseId, courseId).orderByAsc(Chapter::getSortOrder);
        return chapterMapper.selectList(wrapper);
    }

    public Chapter getChapterById(Long chapterId) {
        return chapterMapper.selectById(chapterId);
    }

    public void updateProgress(Long userId, Long courseId, Long chapterId, Integer progressPercent) {
        LambdaQueryWrapper<LearningProgress> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LearningProgress::getUserId, userId)
               .eq(LearningProgress::getCourseId, courseId)
               .eq(LearningProgress::getChapterId, chapterId);
        LearningProgress progress = learningProgressMapper.selectOne(wrapper);

        if (progress == null) {
            progress = new LearningProgress();
            progress.setUserId(userId);
            progress.setCourseId(courseId);
            progress.setChapterId(chapterId);
            progress.setProgressPercent(progressPercent);
            progress.setCompleted(progressPercent >= 100 ? 1 : 0);
            learningProgressMapper.insert(progress);
        } else {
            progress.setProgressPercent(progressPercent);
            progress.setCompleted(progressPercent >= 100 ? 1 : 0);
            learningProgressMapper.updateById(progress);
        }
    }

    public List<LearningProgress> getUserProgress(Long userId, Long courseId) {
        LambdaQueryWrapper<LearningProgress> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LearningProgress::getUserId, userId);
        if (courseId != null) {
            wrapper.eq(LearningProgress::getCourseId, courseId);
        }
        return learningProgressMapper.selectList(wrapper);
    }

    // 管理员方法
    public void saveCourse(Course course) {
        if (course.getId() == null) {
            courseMapper.insert(course);
        } else {
            courseMapper.updateById(course);
        }
    }

    public void deleteCourse(Long id) {
        courseMapper.deleteById(id);
    }

    public void saveChapter(Chapter chapter) {
        if (chapter.getId() == null) {
            chapterMapper.insert(chapter);
        } else {
            chapterMapper.updateById(chapter);
        }
    }

    public void deleteChapter(Long id) {
        chapterMapper.deleteById(id);
    }
}
