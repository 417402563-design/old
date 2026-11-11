package com.antifraud.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("t_assessment_question")
public class AssessmentQuestion {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String question;
    private String optionsJson;    // JSON格式存储选项
    private Integer riskLevel;     // 该选项对应风险分值
    private String category;
    @TableLogic
    private Integer deleted;
}
