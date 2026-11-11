package com.antifraud.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("t_fraud_case")
public class FraudCase {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String caseType;
    private String description;
    private String fraudProcess;
    private String preventionTips;
    private String coverImage;
    private Integer views;
    private Integer status;       // 0隐藏 1显示
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;
}
