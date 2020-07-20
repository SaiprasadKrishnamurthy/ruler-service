package com.github.saiprasadkrishnamurthy.ruler.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.*;

/**
 * A canonical representation of a Rule.
 *
 * @author Sai.
 */
@Document
@Data
public class Rule implements Serializable {

    private static final long serialVersionUID = 1234567L;

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
    private Map<String, Object> metadata = new HashMap<>();
    private Map<String, Object> selectionAttributes = new HashMap<>();
    private String content = "<span />";

    public String getCondition() {
        return "com.github.saiprasadkrishnamurthy.ruler.util.Functions.preconditions(doc.ctx, doc.precondition) && (" + condition + ")";
    }
}
