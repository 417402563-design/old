package com.antifraud.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class AssessmentSubmitDTO {
    private List<Map<String, Object>> answers;
}
