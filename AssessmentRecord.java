package com.antifraud.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("t_assessment_record")
public class AssessmentRecord {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Integer score;
    private String riskLevel;      // low/medium/high
    private String suggestions;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
