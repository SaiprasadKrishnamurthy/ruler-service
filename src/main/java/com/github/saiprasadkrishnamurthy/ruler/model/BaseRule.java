package com.github.saiprasadkrishnamurthy.ruler.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.*;

@Data
public abstract class BaseRule {
    @Id
    private String id = UUID.randomUUID().toString();
    private String name;
    private String description;
    private String author;
    private String category;
    private long created;
    private long lastModified;
    private boolean enabled = true;
    private RuleModeType modeType = RuleModeType.Preview;
    private int priority = 1;
    private RuleType ruleType = RuleType.Primary;
    private String condition;
    private String alternateFor;
    private List<String> overrideFor = new ArrayList<>();
    private Map<String, Object> metadata = new HashMap<>();
    private Map<String, Object> selectionAttributes = new HashMap<>();

}
