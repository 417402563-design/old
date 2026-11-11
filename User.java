package com.antifraud.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("t_user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String realName;
    private String phone;
    private Integer age;
    private Integer gender;       // 0未知 1男 2女
    private String avatar;
    private String role;          // elderly/family/admin
    private Integer accessibilityMode; // 适老化模式 0关闭 1开启
    private Integer status;       // 0禁用 1启用
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;
}
