package com.github.saiprasadkrishnamurthy.ruler.service;

import com.github.saiprasadkrishnamurthy.ruler.model.*;
import com.github.saiprasadkrishnamurthy.ruler.repository.RuleRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DefaultRuleManagementService implements RuleManagementService {

    private final RuleRepository ruleRepository;
    private final MessagePublisher messagePublisher;

    public DefaultRuleManagementService(final RuleRepository ruleRepository, final MessagePublisher messagePublisher) {
        this.ruleRepository = ruleRepository;
        this.messagePublisher = messagePublisher;
    }

    @Override
    public void saveOrUpdateRule(final Rule rule) {
        if (rule.getRuleType() == RuleType.Alternate) {
            List<String> actions = new ArrayList<>(rule.getActions());
            actions.add("doc.ctx.alternateRulesMapping.put(\"" + rule.getName() + "\"" + ",\"" + rule.getAlternateFor() + "\")");
            String condition = "doc.ctx.unmatchedRules contains '" + rule.getAlternateFor() + "'";
            rule.setPriority(100);
            rule.setCondition(condition);
            rule.setActions(actions);
        } else if (rule.getRuleType() == RuleType.Override) {
            List<String> actions = new ArrayList<>(rule.getActions());
            actions.add("doc.ctx.overrideRulesMapping.put(\"" + rule.getName() + "\"" + ",\"" + rule.getOverrideFor() + "\")");
            rule.setActions(actions);
        }
        ruleRepository.save(rule);
        messagePublisher.broadcastRuleStateChanges(rule);
    }

    @Override
    public void promoteRuleToLive(String ruleId) {
    }

    @Override
    public void saveOrUpdateRuleSet(RuleSet ruleSet) {
    }
}
