package com.github.saiprasadkrishnamurthy.ruler.service;

import com.github.saiprasadkrishnamurthy.ruler.model.Document;
import com.github.saiprasadkrishnamurthy.ruler.model.RuleAudit;
import com.github.saiprasadkrishnamurthy.ruler.model.RuleEvaluationContext;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.RuleListener;

public class RuleExecListener implements RuleListener {
    @Override
    public void afterEvaluate(final Rule rule, final Facts facts, final boolean evaluationResult) {
        Document doc = facts.get("doc");
        RuleEvaluationContext ctx = doc.getCtx();
        RuleAudit ruleAudit = new RuleAudit(ctx.getTransactionId(), rule.getName(), evaluationResult, System.currentTimeMillis());
        ctx.getAudits().add(ruleAudit);
        if (!evaluationResult) {
            ctx.getUnmatchedRules().add(rule.getName());
        }
    }
}
