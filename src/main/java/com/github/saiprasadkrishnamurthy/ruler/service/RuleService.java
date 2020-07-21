package com.github.saiprasadkrishnamurthy.ruler.service;

import com.github.saiprasadkrishnamurthy.ruler.model.Rule;
import com.github.saiprasadkrishnamurthy.ruler.model.RuleSet;
import com.github.saiprasadkrishnamurthy.ruler.repository.RuleRepository;
import com.github.saiprasadkrishnamurthy.ruler.repository.RuleSetRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class RuleService {
    private final RuleRepository ruleRepository;
    private final RuleSetRepository ruleSetRepository;

    public RuleService(final RuleRepository ruleRepository, final RuleSetRepository ruleSetRepository) {
        this.ruleRepository = ruleRepository;
        this.ruleSetRepository = ruleSetRepository;
    }

    @Cacheable("rules")
    public Rule findByName(final String name) {
        return ruleRepository.findByName(name).orElse(null);
    }

    @Cacheable("ruleSet")
    public RuleSet findRuleSetByName(final String name) {
        return ruleSetRepository.findByName(name).orElse(null);
    }
}
