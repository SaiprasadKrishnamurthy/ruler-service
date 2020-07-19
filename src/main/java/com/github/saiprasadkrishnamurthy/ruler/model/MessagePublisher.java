package com.github.saiprasadkrishnamurthy.ruler.model;

public interface MessagePublisher {
    void broadcastRuleStateChanges(Rule rule);
}
