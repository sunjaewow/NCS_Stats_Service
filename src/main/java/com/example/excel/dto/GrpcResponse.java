package com.example.excel.dto;

public class GrpcResponse {
    private final String startTime;
    private final int adCnt;
    private final int cdCnt;

    public GrpcResponse(String startTime, int adCnt, int cdCnt) {
        this.startTime = startTime;
        this.adCnt = adCnt;
        this.cdCnt = cdCnt;
    }

    public String getStartTime() {
        return startTime;
    }

    public int getAdCnt() {
        return adCnt;
    }

    public int getCdCnt() {
        return cdCnt;
    }
}