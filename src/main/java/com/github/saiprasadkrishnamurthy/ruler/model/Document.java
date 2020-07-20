package com.github.saiprasadkrishnamurthy.ruler.model;

import lombok.Data;

/**
 * A canonical representation of a Document that acts as a fact to the rule engine.
 * @author Sai.
 */

@Data
public class Document {
    private RuleEvaluationContext ctx = new RuleEvaluationContext();
    private Object payload;
    private String precondition;
}
