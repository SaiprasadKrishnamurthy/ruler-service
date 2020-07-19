package com.github.saiprasadkrishnamurthy.ruler.service;

import com.github.saiprasadkrishnamurthy.ruler.model.Document;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.RuleListener;

public class RuleExecListener implements RuleListener {
    @Override
    public void afterEvaluate(Rule rule, Facts facts, boolean evaluationResult) {
        Document doc = facts.get("doc");
        if (!evaluationResult) {
            doc.getCtx().getUnmatchedRules().add(rule.getName());
        } else {
            doc.getCtx().getMatchedRules().add(rule.getName());
        }
    }
}
