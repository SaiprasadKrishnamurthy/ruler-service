package com.github.saiprasadkrishnamurthy.ruler.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.saiprasadkrishnamurthy.ruler.model.Rule;
import com.github.saiprasadkrishnamurthy.ruler.model.RuleEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RedisRuleStateChangedListener implements MessageListener {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final RuleEngine ruleEngine;

    public RedisRuleStateChangedListener(final RuleEngine ruleEngine) {
        this.ruleEngine = ruleEngine;
    }

    @Override
    public void onMessage(final Message message, final byte[] bytes) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("RedisRuleStateChangedListener - {}", message.toString());
            }
            ruleEngine.load(OBJECT_MAPPER.readValue(message.toString(), Rule.class));
        } catch (Exception ex) {
            log.error(" Error while loading Rule: " + message.toString(), ex);
        }
    }
}
