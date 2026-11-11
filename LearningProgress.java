package com.antifraud.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("t_learning_progress")
public class LearningProgress {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long courseId;
    private Long chapterId;
    private Integer progressPercent;
    private Integer completed;    // 0否 1是
    private LocalDateTime lastLearnTime;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
