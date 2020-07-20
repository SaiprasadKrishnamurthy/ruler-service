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
    private Set<String> matchedRules = new HashSet<>();
    private Set<String> resultantRules = new HashSet<>();
    private Set<String> preconditionFailedRules = new HashSet<>();
    private Map<String, String> alternateRulesMapping = new HashMap<>();
    private Map<String, String> overrideRulesMapping = new HashMap<>();
    private Map<String, Object> metadata = new HashMap<>();
    private Map<String, Object> selectionAttributes = new HashMap<>();
    private List<RuleAudit> audits = new ArrayList<>();
    private Rule currentRule;
    private long totalTimeTakenInMillis;

    public List<RuleAudit> getAudits() {
        audits.sort(Comparator.comparingLong(RuleAudit::getTimestamp));
        return audits;
    }
}
