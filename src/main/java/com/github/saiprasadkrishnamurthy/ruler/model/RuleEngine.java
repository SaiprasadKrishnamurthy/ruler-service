package com.github.saiprasadkrishnamurthy.ruler.model;

public interface RuleEngine {
    void load(Rule rule);

    void run(Document document, RuleModeType modeType);
}
