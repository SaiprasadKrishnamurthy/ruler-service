package com.github.saiprasadkrishnamurthy.ruler.service;

import com.github.saiprasadkrishnamurthy.ruler.model.Rule;
import com.github.saiprasadkrishnamurthy.ruler.model.RuleEvaluationContext;
import org.jeasy.rules.support.RuleDefinition;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public final class RuleEngineTransformers {

    private RuleEngineTransformers() {
    }

    public static Function<Rule, RuleDefinition> transformToEasyRuleDefinition() {
        return rule -> {
            RuleDefinition root = new RuleDefinition();
            root.setName(rule.getName());
            root.setDescription(rule.getDescription());
            root.setName(rule.getName());
            root.setCompositeRuleType("ConditionalRuleGroup");
            root.setPriority(rule.getPriority());
            RuleDefinition child = new RuleDefinition();
            child.setCondition(rule.getCondition());
            child.setPriority(rule.getPriority());
            child.setCondition(rule.getCondition());
            // generic actions.
            List<String> _actions = new ArrayList<>(rule.getActions());
            if (_actions.isEmpty()) {
                _actions.add("1 == 1");
            }
            child.setActions(_actions);
            root.setComposingRules(Collections.singletonList(child));
            return root;
        };
    }

    public static Consumer<RuleEvaluationContext> applyOverrides() {
        return ruleEvaluationContext -> {
            Set<String> matchedRules = ruleEvaluationContext.getMatchedRules();
            Map<String, String> overrideRulesMapping = ruleEvaluationContext.getOverrideRulesMapping();
            Map<String, String> alternateRulesMapping = ruleEvaluationContext.getAlternateRulesMapping();
            overrideRulesMapping.forEach((key, value) -> {
                if (matchedRules.contains(value)) {
                    ruleEvaluationContext.getMatchedRules().remove(value);
                }
                alternateRulesMapping.forEach((k, v) -> {
                    if ((value.equals(k) || value.equals(v))) {
                        matchedRules.remove(v);
                        matchedRules.remove(k);
                    }
                });
            });
        };
    }
}
