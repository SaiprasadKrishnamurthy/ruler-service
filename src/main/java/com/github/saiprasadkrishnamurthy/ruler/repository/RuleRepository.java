package com.github.saiprasadkrishnamurthy.ruler.repository;

import com.github.saiprasadkrishnamurthy.ruler.model.Rule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleRepository extends MongoRepository<Rule, String> {
}
