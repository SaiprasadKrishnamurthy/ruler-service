package com.github.saiprasadkrishnamurthy.ruler.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A canonical representation of a SubRule which can only exist inside a RuleSet.
 *
 * @author Sai.
 */
@Data
public class SubRule implements Serializable {
    private static final long serialVersionUID = 1234567L;
    private String condition;
    private List<String> actions = new ArrayList<>();
    private String content = "<span />";

    public String getCondition() {
        return "com.github.saiprasadkrishnamurthy.ruler.util.Functions.preconditions(doc.ctx, doc.precondition) && (" + condition + ")";
    }
}
