package com.github.saiprasadkrishnamurthy.ruler.service;

import com.github.saiprasadkrishnamurthy.ruler.model.*;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Sai.
 */
@Service
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
        Map<String, String> responseContents = new LinkedHashMap<>();
        document.getCtx().getResultantRules().forEach(n -> {
            Rule r = ruleService.findByName(n);
            if (r == null) {
                RuleSet ruleSet = ruleService.findRuleSetByName(n);
                ruleSet.getRules().forEach(sr -> responseContents.put(ruleSet.getName(), responseRenderer.render(ruleSet.getName(), sr.getContent(), document.getPayload())));
            } else {
                responseContents.put(r.getName(), responseRenderer.render(r.getName(), r.getContent(), document.getPayload()));
            }
        });
        return new RuleEvaluationResponse(document.getCtx().getTransactionId(), document.getCtx(), responseContents);
    }
}
