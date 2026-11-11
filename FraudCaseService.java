package com.antifraud.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.antifraud.common.PageResult;
import com.antifraud.entity.FraudCase;
import com.antifraud.mapper.FraudCaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FraudCaseService {

    @Autowired
    private FraudCaseMapper fraudCaseMapper;

    public PageResult<FraudCase> listCases(int page, int size, String caseType, String keyword) {
        LambdaQueryWrapper<FraudCase> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FraudCase::getStatus, 1);
        if (caseType != null && !caseType.isEmpty()) {
            wrapper.eq(FraudCase::getCaseType, caseType);
        }
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(FraudCase::getTitle, keyword);
        }
        wrapper.orderByDesc(FraudCase::getViews);
        Page<FraudCase> pageResult = fraudCaseMapper.selectPage(new Page<>(page, size), wrapper);
        return new PageResult<>(pageResult.getRecords(), pageResult.getTotal(), pageResult.getPages(), pageResult.getCurrent(), pageResult.getSize());
    }

    public FraudCase getCaseDetail(Long id) {
        FraudCase fraudCase = fraudCaseMapper.selectById(id);
        if (fraudCase != null) {
            // 浏览量+1
            fraudCase.setViews(fraudCase.getViews() + 1);
            fraudCaseMapper.updateById(fraudCase);
        }
        return fraudCase;
    }

    public void saveCase(FraudCase fraudCase) {
        if (fraudCase.getId() == null) {
            fraudCaseMapper.insert(fraudCase);
        } else {
            fraudCaseMapper.updateById(fraudCase);
        }
    }

    public void deleteCase(Long id) {
        fraudCaseMapper.deleteById(id);
    }
}
