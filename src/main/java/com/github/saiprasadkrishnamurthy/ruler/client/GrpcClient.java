package com.github.saiprasadkrishnamurthy.ruler.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.saiprasadkrishnamurthy.ruler.rpc.RuleExecutionRequest;
import com.github.saiprasadkrishnamurthy.ruler.rpc.RuleExecutionResponse;
import com.github.saiprasadkrishnamurthy.ruler.rpc.RuleExecutionServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;

import java.util.HashMap;
import java.util.Map;

public class GrpcClient {
    public static void main(String[] args) throws Exception {
        // Create rules.
        ManagedChannel channel = NettyChannelBuilder.forAddress("localhost", 6790)
                .usePlaintext()
                .build();
        RuleExecutionServiceGrpc.RuleExecutionServiceBlockingStub helloServiceStub = RuleExecutionServiceGrpc
                .newBlockingStub(channel);
        Map<String, Object> fact = new HashMap<>();
        fact.put("active", false);
        fact.put("customerMembership", "Gold");
        fact.put("greeting", "Hello Shubha!");
        String p = new ObjectMapper().writeValueAsString(fact);
        RuleExecutionRequest rq = RuleExecutionRequest.newBuilder()
                .setRuleModeType(RuleExecutionRequest.RuleModeType.Preview)
                .setPayloadJson(p)
                .build();
        RuleExecutionResponse response = helloServiceStub.execute(rq);
        System.out.println(response);
        // assert response.

        // Delete rules.
        Thread.sleep(10000);
    }
}