package com.github.saiprasadkrishnamurthy.ruler.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.saiprasadkrishnamurthy.ruler.model.Rule;
import com.github.saiprasadkrishnamurthy.ruler.model.RuleEngine;
import com.github.saiprasadkrishnamurthy.ruler.model.RuleSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RedisRuleSetStateChangedListener implements MessageListener {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final RuleEngine ruleEngine;

    public RedisRuleSetStateChangedListener(final RuleEngine ruleEngine) {
        this.ruleEngine = ruleEngine;
    }

    @Override
    public void onMessage(final Message message, final byte[] bytes) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("RedisRuleSetStateChangedListener - {}", message.toString());
            }
            ruleEngine.load(OBJECT_MAPPER.readValue(message.toString(), RuleSet.class));
        } catch (Exception ex) {
            log.error(" Error while loading Rule: " + message.toString(), ex);
        }
    }
}
