package com.github.saiprasadkrishnamurthy.ruler.model;

import lombok.Data;

import java.util.*;

/**
 * A Mutable object to record various state changes during rule evaluations.
 *
 * @author Sai.
 */
@Data
public class RuleEvaluationContext {
    private String transactionId = UUID.randomUUID().toString();
    private Set<String> unmatchedRules = new HashSet<>();
    private Map<String, String> alternateRulesMapping = new HashMap<>();
    private Map<String, String> overrideRulesMapping = new HashMap<>();
    private Map<String, Object> metadata = new HashMap<>();
    private List<RuleAudit> audits = new ArrayList<>();

    public List<RuleAudit> getAudits() {
        audits.sort(Comparator.comparingLong(RuleAudit::getTimestamp));
        return audits;
    }
}
