package com.antifraud.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("t_alert")
public class Alert {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String alertType;     // system/manual/risk
    private Integer isRead;       // 0未读 1已读
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
