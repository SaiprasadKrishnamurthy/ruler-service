package com.github.saiprasadkrishnamurthy.ruler.model;

public interface ResponseRenderer {
    String render(final String ruleName, final String content, Object payload);
}
