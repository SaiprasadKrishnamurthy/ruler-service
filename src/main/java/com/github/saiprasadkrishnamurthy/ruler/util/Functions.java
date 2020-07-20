package com.github.saiprasadkrishnamurthy.ruler.util;

import com.github.saiprasadkrishnamurthy.ruler.model.RuleEvaluationContext;
import org.mvel2.MVEL;
import org.springframework.util.StringUtils;

import java.util.Map;

public class Functions {
    public static boolean preconditions(final RuleEvaluationContext ctx, final String mvelExpression) {
        if (StringUtils.hasText(mvelExpression)) {
            Boolean result = (Boolean) MVEL.eval(mvelExpression, ctx.getCurrentRule());
            if (result != null && !result) {
                ctx.getPreconditionFailedRules().add(ctx.getCurrentRule().getName());
                return false;
            }
        }
        // Check the metadata.
        Map<String, Object> runtimeAttrs = ctx.getSelectionAttributes();
        Map<String, Object> ruleAttrss = ctx.getCurrentRule().getSelectionAttributes();
        if (!runtimeAttrs.isEmpty()) {
            for (Map.Entry<String, Object> e : runtimeAttrs.entrySet()) {
                if (e.getValue().equals(ruleAttrss.get(e.getKey()))) {
                    return true;
                }
            }
            ctx.getPreconditionFailedRules().add(ctx.getCurrentRule().getName());
            return false;
        }
        return true;
    }

    public static boolean preConditionsHasNotFailed(final RuleEvaluationContext ruleEvaluationContext) {
        return !ruleEvaluationContext.getPreconditionFailedRules().contains(ruleEvaluationContext.getCurrentRule().getName());
    }
}
