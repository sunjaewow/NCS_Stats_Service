package com.example.excel.service;

import com.example.excel.ContentStatsResponseList;
import com.example.excel.dto.GrpcResponse;
import com.example.excel.ContentStatsServiceGrpc;
import com.example.excel.ContentStatsRequest;
import com.example.excel.ContentStatsResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Service;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;

@Service
public class GrpcClientService {
    private ManagedChannel channel;
    private final ContentStatsServiceGrpc.ContentStatsServiceBlockingStub blockingStub;

    public GrpcClientService() {
        this.channel = ManagedChannelBuilder.forAddress("localhost", 50070)
                .usePlaintext()
                .build();
        this.blockingStub = ContentStatsServiceGrpc.newBlockingStub(channel);
    }

    @PreDestroy
    public void shutdown() {
        if (channel != null) {
            channel.shutdown();
        }
    }

    public List<GrpcResponse> sendMessage(String startTime, int contentId) {
        ContentStatsRequest request = ContentStatsRequest.newBuilder()
                .setStartTime(startTime)
                .setContentId(contentId)
                .build();

        // ContentStatsResponseList에서 여러 개의 ContentStatsResponse를 받음
        ContentStatsResponseList responseList = blockingStub.getContentStats(request);

        List<GrpcResponse> resultList = new ArrayList<>();
        for (ContentStatsResponse response : responseList.getResponsesList()) {
            resultList.add(new GrpcResponse(response.getStartTime(), response.getAdCnt(), response.getCdCnt()));
        }

        return resultList;
    }
}
