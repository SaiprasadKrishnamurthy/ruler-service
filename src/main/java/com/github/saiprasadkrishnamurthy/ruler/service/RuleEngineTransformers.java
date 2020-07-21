package com.github.saiprasadkrishnamurthy.ruler.service;

import com.github.saiprasadkrishnamurthy.ruler.model.Rule;
import com.github.saiprasadkrishnamurthy.ruler.model.RuleEvaluationContext;
import com.github.saiprasadkrishnamurthy.ruler.model.RuleSet;
import org.jeasy.rules.support.RuleDefinition;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public static Function<RuleSet, RuleDefinition> transformToEasyRuleSetDefinition() {
        return rs -> {
            RuleDefinition root = new RuleDefinition();
            root.setName(rs.getName());
            root.setDescription(rs.getDescription());
            root.setName(rs.getName());
            root.setCompositeRuleType("ConditionalRuleGroup");
            root.setPriority(rs.getPriority());
            List<RuleDefinition> subRuleDefs = rs.getRules().stream().map(sr -> {
                RuleDefinition child = new RuleDefinition();
                child.setCondition(sr.getCondition());
                if (CollectionUtils.isEmpty(sr.getActions())) {
                    child.setActions(Collections.singletonList(" 1 == 1 "));
                } else {
                    child.setActions(sr.getActions());
                }
                return child;
            }).collect(Collectors.toList());
            root.setComposingRules(subRuleDefs);
            return root;
        };
    }

    public static Consumer<RuleEvaluationContext> applyOverrides() {
        return ruleEvaluationContext -> {
            Set<String> resultantRules = ruleEvaluationContext.getResultantRules();
            Map<String, String> overrideRulesMapping = ruleEvaluationContext.getOverrideRulesMapping();
            Map<String, String> alternateRulesMapping = ruleEvaluationContext.getAlternateRulesMapping();
            overrideRulesMapping.forEach((key, value) -> {
                if (resultantRules.contains(value)) {
                    ruleEvaluationContext.getMatchedRules().remove(value);
                }
                alternateRulesMapping.forEach((k, v) -> {
                    if ((value.equals(k) || value.equals(v))) {
                        resultantRules.remove(v);
                        resultantRules.remove(k);
                    }
                });
            });
        };
    }
}
