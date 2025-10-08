package com.mllfjn.simyys.interactive;

public class Info {
    private final TraceableNumber traceableNumber;
    private boolean baoJi;
    public Info(TraceableNumber traceableNumber) {
        this.traceableNumber = traceableNumber;
    }

    public TraceableNumber getTraceableNumber() {
        return traceableNumber;
    }

    public void setBaoJi() {
        baoJi = true;
    }

    public boolean getBaoJi() {
        return baoJi;
    }
}
