package com.github.saiprasadkrishnamurthy.ruler.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class RuleEvaluationResponse {
    private String transactionId;
    private RuleEvaluationContext ruleEvaluationContext;
    private Map<String, String> ruleNameContentMapping = new LinkedHashMap<>();
}
