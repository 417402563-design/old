package com.antifraud.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class QuizSubmitDTO {
    private Long quizId;
    private Integer timeSpent;
    private List<Map<String, Object>> answers;
}
