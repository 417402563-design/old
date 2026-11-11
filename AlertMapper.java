package com.antifraud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.antifraud.entity.Alert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AlertMapper extends BaseMapper<Alert> {}
