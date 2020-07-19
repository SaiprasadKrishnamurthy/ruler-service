package com.github.saiprasadkrishnamurthy.ruler.model;

import lombok.Data;

import java.util.*;

/**
 * A Mutable object to record various state changes during rule evaluations.
 * @author Sai.
 */
@Data
public class RuleEvaluationContext {
    private Set<String> unmatchedRules = new HashSet<>();
    private Set<String> matchedRules = new HashSet<>();
    private Map<String, String> alternateRulesMapping = new HashMap<>();
    private Map<String, String> restrictedRulesMapping = new HashMap<>();
    private Map<String, String> overrideRulesMapping = new HashMap<>();
    private Map<String, Long> ruleTimestamps = new LinkedHashMap<>();
    private Map<String, Object> metadata = new HashMap<>();
}
