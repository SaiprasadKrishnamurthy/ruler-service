package com.github.saiprasadkrishnamurthy.ruler.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A canonical representation of a Rule.
 *
 * @author Sai.
 */
@Document
@Data
public class Rule extends BaseRule implements Serializable {

    private static final long serialVersionUID = 1234567L;
    private List<String> actions = new ArrayList<>();
    private String content = "<span />";

    public String getCondition() {
        return "com.github.saiprasadkrishnamurthy.ruler.util.Functions.preconditions(doc.ctx, doc.precondition) && (" + super.getCondition() + ")";
    }
}
