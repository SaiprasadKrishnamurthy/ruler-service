package com.github.saiprasadkrishnamurthy.ruler.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.saiprasadkrishnamurthy.ruler.rpc.RuleExecutionRequest;
import com.github.saiprasadkrishnamurthy.ruler.rpc.RuleExecutionServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;

import java.util.HashMap;
import java.util.Map;

public class GrpcClient {
    public static void main(String[] args) throws Exception {
        ManagedChannel channel = NettyChannelBuilder.forAddress("localhost", 6790)
                .usePlaintext()
                .build();
        RuleExecutionServiceGrpc.RuleExecutionServiceBlockingStub helloServiceStub = RuleExecutionServiceGrpc
                .newBlockingStub(channel);
        Map<String, Object> payload = new HashMap<>();
        payload.put("active", true);
        String p = new ObjectMapper().writeValueAsString(payload);
        RuleExecutionRequest rq = RuleExecutionRequest.newBuilder().setRuleModeType(RuleExecutionRequest.RuleModeType.Preview).setPayloadJson(p).build();
        System.out.println(helloServiceStub.execute(rq));
        Thread.sleep(10000);
    }
}
