package com.github.saiprasadkrishnamurthy.ruler.service;

import com.github.saiprasadkrishnamurthy.ruler.model.Rule;
import com.github.saiprasadkrishnamurthy.ruler.repository.RuleRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class RuleService {
    private final RuleRepository ruleRepository;

    public RuleService(final RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    @Cacheable("rules")
    public Rule findByName(final String name) {
        return ruleRepository.findByName(name).orElseThrow(() -> new IllegalArgumentException(" No rule found for name: " + name));
    }
}
