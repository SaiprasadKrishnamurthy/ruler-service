package com.github.saiprasadkrishnamurthy.ruler.grpc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.saiprasadkrishnamurthy.ruler.model.*;
import com.github.saiprasadkrishnamurthy.ruler.rpc.RuleExecutionRequest;
import com.github.saiprasadkrishnamurthy.ruler.rpc.RuleExecutionResponse;
import com.github.saiprasadkrishnamurthy.ruler.rpc.RuleExecutionServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@GrpcService
public class RuleExecutionImpl extends RuleExecutionServiceGrpc.RuleExecutionServiceImplBase {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final RuleOrchestrationService ruleOrchestrationService;

    public RuleExecutionImpl(final RuleOrchestrationService ruleOrchestrationService) {
        this.ruleOrchestrationService = ruleOrchestrationService;
    }

    @Override
    public void execute(final RuleExecutionRequest request, final StreamObserver<RuleExecutionResponse> responseObserver) {
        try {
            Map payload = OBJECT_MAPPER.readValue(request.getPayloadJson(), Map.class);
            Map selectionAttrs = OBJECT_MAPPER.readValue(request.getSelectionAttributesMap().toString(), new TypeReference<Map<String, Object>>() {
            });
            StopWatch sw = new StopWatch();
            sw.start();
            RuleEvaluationContext ctx = new RuleEvaluationContext();
            if (StringUtils.hasText(request.getTransactionId())) {
                ctx.setTransactionId(request.getTransactionId());

            }
            Document doc = new Document();
            doc.setPayload(payload);
            doc.setCtx(ctx);
            doc.setPrecondition(request.getPrecondition());
            ctx.setSelectionAttributes(selectionAttrs);
            RuleEvaluationResponse response = ruleOrchestrationService.execute(doc, RuleModeType.valueOf(request.getRuleModeType().name()));
            List<com.github.saiprasadkrishnamurthy.ruler.rpc.RuleAudit> ras = ctx.getAudits().stream().map(r ->
                    com.github.saiprasadkrishnamurthy.ruler.rpc.RuleAudit.newBuilder()
                            .setResult(r.isResult())
                            .setRuleName(r.getRule().getName())
                            .setRuleType(r.getRule().getRuleType().toString())
                            .setTimestamp(r.getTimestamp())
                            .setTransactionId(r.getTransactionId())
                            .setEnabled(r.getRule().isEnabled())
                            .setTimeTakenInMillis(r.getTimeTakenInMillis())
                            .setAlternateFor(r.getRule().getAlternateFor() != null ? r.getRule().getAlternateFor() : "")
                            .addAllOverrideFor(r.getRule().getOverrideFor())
                            .build()
            ).collect(Collectors.toList());
            RuleExecutionResponse rsp = RuleExecutionResponse.newBuilder()
                    .setContext(
                            com.github.saiprasadkrishnamurthy.ruler.rpc.RuleEvaluationContext.newBuilder()
                                    .setTransactionId(ctx.getTransactionId())
                                    .addAllMatchedRules(ctx.getMatchedRules())
                                    .addAllResultantRules(ctx.getResultantRules())
                                    .addAllUnmatchedRules(ctx.getUnmatchedRules())
                                    .addAllAudits(ras)
                                    .build())
                    .putAllRuleNameContentMapping(response.getRuleNameContentMapping())
                    .build();
            responseObserver.onNext(rsp);
            responseObserver.onCompleted();
        } catch (Exception ex) {
            log.error("Error: ", ex);
            responseObserver.onError(ex);
        }
    }
}
