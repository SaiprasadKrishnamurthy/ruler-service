package com.github.saiprasadkrishnamurthy.ruler.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A canonical representation of a RuleSet.
 *
 * @author Sai.
 */
@Document
@Data
public class RuleSet extends BaseRule implements Serializable {
    private List<SubRule> rules = new ArrayList<>();
}
