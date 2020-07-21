package com.github.saiprasadkrishnamurthy.ruler.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.saiprasadkrishnamurthy.ruler.model.MessagePublisher;
import com.github.saiprasadkrishnamurthy.ruler.model.Rule;
import com.github.saiprasadkrishnamurthy.ruler.model.RuleSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RedisMessagePublisher implements MessagePublisher {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final RedisTemplate<String, String> redisTemplate;
    private final String ruleStateChangesTopic;
    private final String ruleSetStateChangesTopic;

    public RedisMessagePublisher(final RedisTemplate<String, String> redisTemplate,
                                 @Value("${ruleStateChangesTopic}") final String ruleStateChangesTopic,
                                 @Value("${ruleSetStateChangesTopic}") final String ruleSetStateChangesTopic) {
        this.redisTemplate = redisTemplate;
        this.ruleStateChangesTopic = ruleStateChangesTopic;
        this.ruleSetStateChangesTopic = ruleSetStateChangesTopic;
    }

    @Override
    public void broadcastRuleStateChanges(final Rule rule) {
        try {
            redisTemplate.convertAndSend(ruleStateChangesTopic, OBJECT_MAPPER.writeValueAsString(rule));
        } catch (Exception e) {
            log.error("Error while broadcasting rule state changes: " + rule, e);
        }
    }

    @Override
    public void broadcastRuleSetStateChanges(final RuleSet ruleSet) {
        try {
            redisTemplate.convertAndSend(ruleSetStateChangesTopic, OBJECT_MAPPER.writeValueAsString(ruleSet));
        } catch (Exception e) {
            log.error("Error while broadcasting ruleset state changes: " + ruleSet, e);
        }
    }
}
