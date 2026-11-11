package com.antifraud.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("t_chapter")
public class Chapter {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long courseId;
    private String title;
    private String contentRich;   // 富文本内容
    private String videoUrl;
    private Integer sortOrder;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableLogic
    private Integer deleted;
}
