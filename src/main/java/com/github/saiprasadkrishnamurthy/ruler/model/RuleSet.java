package com.github.saiprasadkrishnamurthy.ruler.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A canonical representation of a RuleSet.
 * @author Sai.
 */
@Data
public class RuleSet {
    private String id;
    private String name;
    private String description;
    private String author;
    private String category;
    private long created;
    private long lastModified;
    private boolean enabled;
    private RuleModeType modeType = RuleModeType.Preview;
    private int priority = 1;
    private RuleType ruleSetType = RuleType.Primary;
    private String condition;
    private String alternateFor;
    private String overrideFor;
    private List<Rule> rules = new ArrayList<>();
    private Map<String, String> metadata = new HashMap<>();
}
