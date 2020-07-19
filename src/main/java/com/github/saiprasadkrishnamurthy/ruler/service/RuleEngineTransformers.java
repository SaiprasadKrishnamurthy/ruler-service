package com.github.saiprasadkrishnamurthy.ruler.service;

import com.github.saiprasadkrishnamurthy.ruler.model.Rule;
import org.jeasy.rules.support.RuleDefinition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
}
