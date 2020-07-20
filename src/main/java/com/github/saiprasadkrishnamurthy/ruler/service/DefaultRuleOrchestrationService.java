package com.github.saiprasadkrishnamurthy.ruler.service;

import com.github.saiprasadkrishnamurthy.ruler.model.*;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Sai.
 */
public class DefaultRuleOrchestrationService implements RuleOrchestrationService {

    private final RuleEngine ruleEngine;
    private final ResponseRenderer responseRenderer;
    private final RuleService ruleService;

    public DefaultRuleOrchestrationService(final RuleEngine ruleEngine, final ResponseRenderer responseRenderer, final RuleService ruleService) {
        this.ruleEngine = ruleEngine;
        this.responseRenderer = responseRenderer;
        this.ruleService = ruleService;
    }

    @Override
    public RuleEvaluationResponse execute(final Document document, final RuleModeType ruleModeType) {
        // Run.
        ruleEngine.run(document, ruleModeType);
        Map<String, String> responseContents = document.getCtx().getMatchedRules().stream()
                .map(ruleService::findByName)
                .collect(Collectors.toMap(Rule::getName, rule -> responseRenderer.render(rule, document.getPayload())));
        return new RuleEvaluationResponse(document.getCtx().getTransactionId(), document.getCtx(), responseContents);
    }
}
