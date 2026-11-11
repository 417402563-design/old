package com.antifraud.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("t_course")
public class Course {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String category;
    private String description;
    private String coverImage;
    private Integer difficulty;  // 1简单 2中等 3困难
    private Integer duration;    // 总时长(分钟)
    private Integer status;      // 0下架 1上架
    private Integer sortOrder;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;
}
