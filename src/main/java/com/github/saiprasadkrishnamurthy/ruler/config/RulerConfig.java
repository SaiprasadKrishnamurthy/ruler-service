package com.github.saiprasadkrishnamurthy.ruler.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.saiprasadkrishnamurthy.ruler.messaging.RedisRuleStateChangedListener;
import com.github.saiprasadkrishnamurthy.ruler.model.*;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.TimeoutOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Configuration
public class RulerConfig {

    @Autowired
    private RuleEngine ruleEngine;

    @Bean
    public RedisConnectionFactory jedisConnectionFactory(final Environment environment) {
        RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration();
        redisConfiguration.setDatabase(environment.getProperty("redis.database", Integer.class));
        redisConfiguration.setHostName(environment.getProperty("redis.host"));
        redisConfiguration.setPort(environment.getProperty("redis.port", Integer.class));
        redisConfiguration.setPassword(RedisPassword.of(environment.getProperty("redis.password", String.class)));
        ClientOptions clientOptions = ClientOptions.builder()
                .autoReconnect(true)
                .timeoutOptions(TimeoutOptions.builder().fixedTimeout(Duration.ofSeconds(10)).build())
                .requestQueueSize(Integer.MAX_VALUE)
                .build();
        LettuceClientConfiguration clientConfiguration = LettuceClientConfiguration.builder()
                .commandTimeout(Duration.ofSeconds(10))
                .clientOptions(clientOptions)
                .build();
        return new LettuceConnectionFactory(redisConfiguration, clientConfiguration);
    }

    @Bean
    public RedisTemplate redisTemplate(final RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(final RedisConnectionFactory redisConnectionFactory,
                                                        @Value("${ruleStateChangesTopic}") final String topic,
                                                        final RedisRuleStateChangedListener redisRuleStateChangedListener) {
        RedisMessageListenerContainer container
                = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(redisRuleStateChangedListener, new ChannelTopic(topic));
        return container;
    }

    @Bean
    public CommandLineRunner start(final RuleManagementService ruleManagementService) {
        return args -> {
            Rule rule = new Rule();
            rule.setName("my first rule");
            rule.setAuthor("Sai");
            rule.setCategory("Cat1");
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("m1", "v1");
            rule.setMetadata(metadata);
            rule.setContent(" Content is here");
            rule.setPriority(1);
            rule.setCreated(System.currentTimeMillis());
            rule.setLastModified(System.currentTimeMillis());
            rule.setDescription(" Something weired ");
            rule.setEnabled(true);
            rule.setCondition("doc.payload.active == false");
            rule.setRuleType(RuleType.Primary);
            ruleManagementService.saveOrUpdateRule(rule);
            rule = new Rule();
            rule.setName("my alternate rule for first rule");
            rule.setAuthor("Sai");
            rule.setCategory("Cat1");
            metadata.put("m1", "v1");
            rule.setMetadata(metadata);
            rule.setContent(" Content is here");
            rule.setPriority(1);
            rule.setCreated(System.currentTimeMillis());
            rule.setLastModified(System.currentTimeMillis());
            rule.setDescription(" Something weired ");
            rule.setEnabled(true);
            rule.setRuleType(RuleType.Alternate);
            rule.setAlternateFor("my first rule");
            ruleManagementService.saveOrUpdateRule(rule);
            rule = new Rule();
            rule.setName("my override rule for first rule");
            rule.setAuthor("Sai");
            rule.setCategory("Cat1");
            metadata.put("m1", "v1");
            rule.setMetadata(metadata);
            rule.setContent(" Content is here");
            rule.setPriority(1);
            rule.setCondition("1 == 1");
            rule.setCreated(System.currentTimeMillis());
            rule.setLastModified(System.currentTimeMillis());
            rule.setDescription(" Somethng weired ");
            rule.setEnabled(true);
            rule.setRuleType(RuleType.Override);
            rule.setOverrideFor("my first rule");
            ruleManagementService.saveOrUpdateRule(rule);
        };
    }

    @Bean
    public Caffeine caffeineConfig() {
        return Caffeine.newBuilder().expireAfterWrite(365, TimeUnit.DAYS);
    }

    @Bean
    public CacheManager cacheManager(final Caffeine caffeine) {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setCaffeine(caffeine);
        return caffeineCacheManager;
    }

    @Scheduled(fixedDelay = 3000L)
    public void foo() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("active", true);
        Document doc = new Document();
        doc.setPayload(payload);
        ruleEngine.run(doc, RuleModeType.Preview);
        System.out.println(" Audit ");
        System.out.println(" ------------ ");
        doc.getCtx().getAudits().forEach(ra -> {
            System.out.println("\t" + ra.getRule().getName() + " (" + ra.isResult() + ")");
        });
        System.out.println();
        System.out.println("Matched Rules:");
        System.out.println("-------------------");
        System.out.println("\t" + doc.getCtx().getMatchedRules());
        System.out.println("\n\n");
    }
}
