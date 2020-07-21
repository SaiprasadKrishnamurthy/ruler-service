package com.github.saiprasadkrishnamurthy.ruler.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RuleAudit {
    private String transactionId;
    private BaseRule rule;
    private boolean result;
    private long timestamp = System.currentTimeMillis();
}
