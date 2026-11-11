package com.antifraud.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("t_quiz_question")
public class QuizQuestion {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long quizId;
    private String question;
    private String optionsJson;
    private String correctAnswer;
    private String explanation;
    private Integer sortOrder;
    @TableLogic
    private Integer deleted;
}
