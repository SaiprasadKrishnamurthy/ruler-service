package com.github.saiprasadkrishnamurthy.ruler.model;

public interface ResponseRenderer {
    String render(Rule rule, Object payload);
}
