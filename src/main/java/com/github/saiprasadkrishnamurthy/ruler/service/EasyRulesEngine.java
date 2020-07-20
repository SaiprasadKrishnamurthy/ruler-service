package com.github.saiprasadkrishnamurthy.ruler.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.saiprasadkrishnamurthy.ruler.model.Document;
import com.github.saiprasadkrishnamurthy.ruler.model.Rule;
import com.github.saiprasadkrishnamurthy.ruler.model.RuleEngine;
import com.github.saiprasadkrishnamurthy.ruler.model.RuleModeType;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.mvel.MVELRuleFactory;
import org.jeasy.rules.support.RuleDefinition;
import org.jeasy.rules.support.reader.JsonRuleDefinitionReader;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.util.Collections;

import static com.github.saiprasadkrishnamurthy.ruler.service.RuleEngineTransformers.applyOverrides;

/**
 * Rule engine that uses Easy Rules Engine as the underlying RE.
 *
 * @author Sai.
 */
@Service
@Slf4j
public class EasyRulesEngine implements RuleEngine {

    private final DefaultRulesEngine LIVE_INSTANCE = new DefaultRulesEngine();
    private final Rules LIVE_RULES = new Rules();
    private final DefaultRulesEngine PREVIEW_INSTANCE = new DefaultRulesEngine();
    private final Rules PREVIEW_RULES = new Rules();
    private final RuleExecListener ruleExecListener;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public EasyRulesEngine(final RuleExecListener ruleExecListener) {
        this.ruleExecListener = ruleExecListener;
    }

    @Override
    public void load(final Rule rule) {
        try {
            RuleDefinition ruleDefinition = RuleEngineTransformers.transformToEasyRuleDefinition().apply(rule);
            MVELRuleFactory ruleFactory = new MVELRuleFactory(new JsonRuleDefinitionReader());
            DefaultRulesEngine engine = null;
            if (rule.getModeType() == RuleModeType.Preview) {
                org.jeasy.rules.api.Rule _rule = ruleFactory.createRule(new StringReader(OBJECT_MAPPER.writeValueAsString(Collections.singletonList(ruleDefinition))));
                PREVIEW_RULES.unregister(_rule);
                PREVIEW_RULES.register(_rule);
                engine = PREVIEW_INSTANCE;
            } else {
                org.jeasy.rules.api.Rule _rule = ruleFactory.createRule(new StringReader(OBJECT_MAPPER.writeValueAsString(Collections.singletonList(ruleDefinition))));
                LIVE_RULES.unregister(_rule);
                LIVE_RULES.register(_rule);
                engine = LIVE_INSTANCE;
            }
            if (engine.getRuleListeners().isEmpty()) {
                engine.registerRuleListener(ruleExecListener);
            }
        } catch (Exception ex) {
            log.error("Error while loading rule: " + rule, ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void run(final Document document, final RuleModeType modeType) {
        Facts facts = new Facts();
        facts.put("doc", document);
        if (modeType == RuleModeType.Preview) {
            PREVIEW_INSTANCE.fire(PREVIEW_RULES, facts);
        } else {
            LIVE_INSTANCE.fire(LIVE_RULES, facts);
        }
        applyOverrides().accept(document.getCtx());
    }
}
