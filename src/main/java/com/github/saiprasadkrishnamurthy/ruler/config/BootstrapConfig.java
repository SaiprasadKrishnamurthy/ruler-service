package com.github.saiprasadkrishnamurthy.ruler.config;

import com.github.saiprasadkrishnamurthy.ruler.model.RuleEngine;
import com.github.saiprasadkrishnamurthy.ruler.repository.RuleRepository;
import com.github.saiprasadkrishnamurthy.ruler.repository.RuleSetRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class BootstrapConfig {

    @Bean
    public CommandLineRunner loadAllRules(final RuleRepository ruleRepository, final RuleSetRepository ruleSetRepository, final RuleEngine ruleEngine) {
        return args -> {
            log.info(" About to load all rules and rulesets into the Rules Engine ");
            ruleRepository.findAll().forEach(ruleEngine::load);
            ruleSetRepository.findAll().forEach(ruleEngine::load);
            log.info(" Finished Loading all rules and rulesets into the Rules Engine ");
        };
    }
}
