package com.github.saiprasadkrishnamurthy.ruler.model;

public interface RuleManagementService {
    void saveOrUpdateRule(Rule rule);

    void promoteRuleToLive(String ruleId);

    void saveOrUpdateRuleSet(RuleSet ruleSet);
}
