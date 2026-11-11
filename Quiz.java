package com.antifraud.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("t_quiz")
public class Quiz {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String description;
    private Integer totalQuestions;
    private Integer passScore;
    private Integer status;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableLogic
    private Integer deleted;
}
