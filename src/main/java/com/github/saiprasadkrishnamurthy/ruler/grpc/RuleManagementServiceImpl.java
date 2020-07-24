package com.github.saiprasadkrishnamurthy.ruler.grpc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.saiprasadkrishnamurthy.ruler.model.RuleManagementService;
import com.github.saiprasadkrishnamurthy.ruler.model.RuleModeType;
import com.github.saiprasadkrishnamurthy.ruler.model.RuleType;
import com.github.saiprasadkrishnamurthy.ruler.model.SubRule;
import com.github.saiprasadkrishnamurthy.ruler.rpc.Identifier;
import com.github.saiprasadkrishnamurthy.ruler.rpc.Rule;
import com.github.saiprasadkrishnamurthy.ruler.rpc.RuleManagementServiceGrpc;
import com.github.saiprasadkrishnamurthy.ruler.rpc.RuleSet;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@GrpcService
public class RuleManagementServiceImpl extends RuleManagementServiceGrpc.RuleManagementServiceImplBase {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final RuleManagementService ruleManagementService;

    public RuleManagementServiceImpl(final RuleManagementService ruleManagementService) {
        this.ruleManagementService = ruleManagementService;
    }

    @Override
    public void createRule(final Rule request, final StreamObserver<Identifier> responseObserver) {
        try {
            com.github.saiprasadkrishnamurthy.ruler.model.Rule rule = new com.github.saiprasadkrishnamurthy.ruler.model.Rule();
            rule.setName(request.getName());
            rule.setAuthor(request.getAuthor());
            rule.setCategory(request.getCategory());
            rule.setContent(request.getContent());
            rule.setActions(request.getActionsList());
            rule.setCondition(request.getCondition());
            rule.setPriority(request.getPriority());
            rule.setAlternateFor(request.getAlternateFor());
            rule.setOverrideFor(request.getOverrideForList());
            rule.setRuleType(RuleType.valueOf(request.getRuleType().name()));
            rule.setEnabled(request.getEnabled());
            rule.setDescription(request.getDescription());
            rule.setId(UUID.randomUUID().toString());
            rule.setCreated(System.currentTimeMillis());
            rule.setLastModified(System.currentTimeMillis());
            rule.setAuthor(request.getAuthor());
            rule.setModeType(RuleModeType.valueOf(request.getRuleModeType().name()));
            rule.setMetadata(OBJECT_MAPPER.readValue(request.getMetadataJson(), new TypeReference<>() {
            }));
            rule.setSelectionAttributes(OBJECT_MAPPER.readValue(request.getSelectionAttributesJson(), new TypeReference<>() {
            }));
            rule.setCategory(request.getCategory());
            ruleManagementService.saveOrUpdateRule(rule);
            responseObserver.onNext(Identifier.newBuilder().setId(rule.getId()).build());
            responseObserver.onCompleted();
        } catch (Exception ex) {
            responseObserver.onError(ex);
        }
    }

    @Override
    public void getRule(Identifier request, StreamObserver<Rule> responseObserver) {
        super.getRule(request, responseObserver);
    }

    @Override
    public void createRuleSet(RuleSet request, StreamObserver<Identifier> responseObserver) {
        try {
            com.github.saiprasadkrishnamurthy.ruler.model.RuleSet ruleSet = new com.github.saiprasadkrishnamurthy.ruler.model.RuleSet();
            ruleSet.setName(request.getName());
            ruleSet.setAuthor(request.getAuthor());
            ruleSet.setCategory(request.getCategory());
            ruleSet.setCondition(request.getCondition());
            ruleSet.setPriority(request.getPriority());
            ruleSet.setAlternateFor(request.getAlternateFor());
            ruleSet.setOverrideFor(request.getOverrideForList());
            ruleSet.setRuleType(RuleType.valueOf(request.getRuleType().name()));
            ruleSet.setEnabled(request.getEnabled());
            ruleSet.setDescription(request.getDescription());
            ruleSet.setId(UUID.randomUUID().toString());
            ruleSet.setCreated(System.currentTimeMillis());
            ruleSet.setLastModified(System.currentTimeMillis());
            ruleSet.setAuthor(request.getAuthor());
            ruleSet.setModeType(RuleModeType.valueOf(request.getRuleModeType().name()));
            ruleSet.setMetadata(OBJECT_MAPPER.readValue(request.getMetadataJson(), new TypeReference<>() {
            }));
            ruleSet.setSelectionAttributes(OBJECT_MAPPER.readValue(request.getSelectionAttributesJson(), new TypeReference<>() {
            }));
            ruleSet.setCategory(request.getCategory());
            List<SubRule> subrules = request.getRulesList().stream().map(sr -> {
                SubRule s = new SubRule();
                s.setCondition(sr.getCondition());
                s.setEnabled(sr.getEnabled());
                s.setActions(sr.getActionsList());
                s.setContent(sr.getContent());
                return s;
            }).collect(Collectors.toList());
            ruleSet.setRules(subrules);
            ruleManagementService.saveOrUpdateRuleSet(ruleSet);
            responseObserver.onNext(Identifier.newBuilder().setId(ruleSet.getId()).build());
            responseObserver.onCompleted();
        } catch (Exception ex) {
            responseObserver.onError(ex);
        }
    }

    @Override
    public void getRuleSet(Identifier request, StreamObserver<RuleSet> responseObserver) {
        super.getRuleSet(request, responseObserver);
    }
}
