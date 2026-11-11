package com.antifraud.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("t_quiz_record")
public class QuizRecord {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long quizId;
    private Integer score;
    private String answersJson;
    private Integer passed;       // 0未通过 1通过
    private Integer timeSpent;    // 用时(秒)
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
