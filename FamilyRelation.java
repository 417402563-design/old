package com.antifraud.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("t_family_relation")
public class FamilyRelation {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long elderlyId;
    private Long familyMemberId;
    private String relation;      // 子女/配偶/孙辈等
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
