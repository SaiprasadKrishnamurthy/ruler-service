package com.github.saiprasadkrishnamurthy.ruler.repository;

import com.github.saiprasadkrishnamurthy.ruler.model.RuleSet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RuleSetRepository extends MongoRepository<RuleSet, String> {
    Optional<RuleSet> findByName(String name);
}
