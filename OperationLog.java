package com.antifraud.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("t_operation_log")
public class OperationLog {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long adminId;
    private String action;
    private String target;
    private String detail;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
