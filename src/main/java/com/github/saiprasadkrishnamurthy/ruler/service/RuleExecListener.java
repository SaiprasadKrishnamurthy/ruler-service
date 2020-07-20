package com.github.saiprasadkrishnamurthy.ruler.service;

import com.github.saiprasadkrishnamurthy.ruler.model.Document;
import com.github.saiprasadkrishnamurthy.ruler.model.RuleAudit;
import com.github.saiprasadkrishnamurthy.ruler.model.RuleEvaluationContext;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.RuleListener;
import org.springframework.stereotype.Service;

@Service
public class RuleExecListener implements RuleListener {

    private final RuleService ruleService;

    public RuleExecListener(final RuleService ruleService) {
        this.ruleService = ruleService;
    }


    @Override
    public boolean beforeEvaluate(final Rule rule, final Facts facts) {
        Document doc = facts.get("doc");
        RuleEvaluationContext ctx = doc.getCtx();
        ctx.setCurrentRule(ruleService.findByName(rule.getName()));
        return true;
    }

    @Override
    public void afterEvaluate(final Rule rule, final Facts facts, final boolean evaluationResult) {
        Document doc = facts.get("doc");
        RuleEvaluationContext ctx = doc.getCtx();
        RuleAudit ruleAudit = new RuleAudit(ctx.getTransactionId(), ruleService.findByName(rule.getName()), evaluationResult, System.currentTimeMillis());
        ctx.getAudits().add(ruleAudit);
        if (!evaluationResult) {
            ctx.getUnmatchedRules().add(rule.getName());
        } else {
            ctx.getMatchedRules().add(rule.getName());
            ctx.getResultantRules().add(rule.getName());
        }
    }
}
