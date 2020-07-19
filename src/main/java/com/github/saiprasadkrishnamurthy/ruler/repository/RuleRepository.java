package com.github.saiprasadkrishnamurthy.ruler.repository;

import com.github.saiprasadkrishnamurthy.ruler.model.Rule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RuleRepository extends MongoRepository<Rule, String> {
    Optional<Rule> findByName(String name);
}
