package com.antifraud.controller;

import com.antifraud.common.Result;
import com.antifraud.service.ReportService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/personal")
    public Result<Map<String, Object>> personalReport(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(reportService.getPersonalReport(userId));
    }

    @GetMapping("/platform")
    public Result<Map<String, Object>> platformReport() {
        return Result.success(reportService.getPlatformReport());
    }
}
