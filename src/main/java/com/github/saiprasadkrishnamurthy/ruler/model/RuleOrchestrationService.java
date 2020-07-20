package com.github.saiprasadkrishnamurthy.ruler.model;

public interface RuleOrchestrationService {
    RuleEvaluationResponse execute(Document document, RuleModeType ruleModeType);
}
