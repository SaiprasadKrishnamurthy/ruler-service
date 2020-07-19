package com.github.saiprasadkrishnamurthy.ruler.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

/**
 * A canonical representation of a Rule.
 *
 * @author Sai.
 */
@Document
@Data
public class Rule {
    @Id
    private String id = UUID.randomUUID().toString();
    private String name;
    private String description;
    private String author;
    private String category;
    private long created;
    private long lastModified;
    private boolean enabled;
    private RuleModeType modeType = RuleModeType.Preview;
    private int priority = 1;
    private RuleType ruleType = RuleType.Primary;
    private String alternateFor;
    private String overrideFor;
    private String condition;
    private List<String> actions = new ArrayList<>();
    private Map<String, String> metadata = new HashMap<>();
    private String content = "";
}
