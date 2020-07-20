package com.github.saiprasadkrishnamurthy.ruler.render;

import com.github.saiprasadkrishnamurthy.ruler.model.ResponseRenderer;
import com.github.saiprasadkrishnamurthy.ruler.model.Rule;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sai.
 */
@Service
@Slf4j
public class PebbleResponseRenderer implements ResponseRenderer {

    @Override
    public String render(final Rule rule, final Object payload) {
        try {
            PebbleEngine engine = new PebbleEngine.Builder().build();
            PebbleTemplate compiledTemplate = engine.getLiteralTemplate(rule.getContent());
            Map<String, Object> context = new HashMap<>();
            context.put("doc", payload);
            Writer writer = new StringWriter();
            compiledTemplate.evaluate(writer, context);
            return writer.toString();
        } catch (Exception ex) {
            log.error(" Error while rendering rule content for: " + rule.getName(), ex);
            throw new RuntimeException(ex);
        }
    }
}
