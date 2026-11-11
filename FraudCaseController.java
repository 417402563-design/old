package com.antifraud.controller;

import com.antifraud.common.PageResult;
import com.antifraud.common.Result;
import com.antifraud.entity.FraudCase;
import com.antifraud.service.FraudCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/case")
public class FraudCaseController {

    @Autowired
    private FraudCaseService fraudCaseService;

    @GetMapping("/list")
    public Result<PageResult<FraudCase>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String caseType,
            @RequestParam(required = false) String keyword) {
        return Result.success(fraudCaseService.listCases(page, size, caseType, keyword));
    }

    @GetMapping("/{id}")
    public Result<FraudCase> getDetail(@PathVariable Long id) {
        return Result.success(fraudCaseService.getCaseDetail(id));
    }
}
