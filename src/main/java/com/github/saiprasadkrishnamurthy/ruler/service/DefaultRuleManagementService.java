package com.github.saiprasadkrishnamurthy.ruler.service;

import com.github.saiprasadkrishnamurthy.ruler.model.*;
import com.github.saiprasadkrishnamurthy.ruler.repository.RuleRepository;
import com.github.saiprasadkrishnamurthy.ruler.repository.RuleSetRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Sai.
 */
@Service
public class DefaultRuleManagementService implements RuleManagementService {

    private final RuleRepository ruleRepository;
    private final MessagePublisher messagePublisher;
    private final RuleSetRepository ruleSetRepository;

    public DefaultRuleManagementService(final RuleRepository ruleRepository,
                                        final MessagePublisher messagePublisher,
                                        final RuleSetRepository ruleSetRepository) {
        this.ruleRepository = ruleRepository;
        this.messagePublisher = messagePublisher;
        this.ruleSetRepository = ruleSetRepository;
    }

    @Override
    public void saveOrUpdateRule(final Rule rule) {
        if (rule.getRuleType() == RuleType.Alternate) {
            List<String> actions = new ArrayList<>(rule.getActions());
            actions.add("doc.ctx.alternateRulesMapping.put(\"" + rule.getName() + "\"" + ",\"" + rule.getAlternateFor() + "\")");
            String condition = "com.github.saiprasadkrishnamurthy.ruler.util.Functions.preConditionsHasNotFailed(doc.ctx) && doc.ctx.unmatchedRules contains '" + rule.getAlternateFor() + "'";
            // Lower the priority of this rule with the primary rule. This is important for the correct firing order.
            ruleRepository.findByName(rule.getAlternateFor()).ifPresent(r -> rule.setPriority(r.getPriority() + 10));
            rule.setCondition(condition);
            rule.setActions(actions);
        } else if (rule.getRuleType() == RuleType.Override) {
            List<String> actions = new ArrayList<>(rule.getActions());
            rule.getOverrideFor().forEach(o -> actions.add("doc.ctx.overrideRulesMapping.put(\"" + rule.getName() + "\"" + ",\"" + o + "\")"));
            rule.setActions(actions);
        }
        ruleRepository.save(rule);
        messagePublisher.broadcastRuleStateChanges(rule);
    }

    @Override
    public void promoteRuleToLive(String ruleId) {
    }

    @Override
    public void saveOrUpdateRuleSet(final RuleSet ruleSet) {
        ruleSet.getRules().forEach(r -> r.setEnabled(ruleSet.isEnabled()));
        if (ruleSet.getRuleType() == RuleType.Alternate) {
            ruleSet.getRules().forEach(r -> r.getActions().add("doc.ctx.alternateRulesMapping.put(\"" + ruleSet.getName() + "\"" + ",\"" + ruleSet.getAlternateFor() + "\")"));
            String condition = "com.github.saiprasadkrishnamurthy.ruler.util.Functions.preConditionsHasNotFailed(doc.ctx) && doc.ctx.unmatchedRules contains '" + ruleSet.getAlternateFor() + "'";
            ruleSet.getRules().forEach(r -> r.setCondition(condition));
            ruleSetRepository.findByName(ruleSet.getAlternateFor()).ifPresent(r -> ruleSet.setPriority(r.getPriority() + 10));
        } else if (ruleSet.getRuleType() == RuleType.Override) {
            ruleSet.getOverrideFor().forEach(o -> ruleSet.getRules().forEach(r -> r.getActions().add("doc.ctx.overrideRulesMapping.put(\"" + ruleSet.getName() + "\"" + ",\"" + o + "\")")));
        }
        ruleSet.getRules().add(0, new SubRule(ruleSet.getCondition(), ruleSet.isEnabled(), Collections.emptyList(), ""));
        ruleSetRepository.save(ruleSet);
        messagePublisher.broadcastRuleSetStateChanges(ruleSet);

    }
}
