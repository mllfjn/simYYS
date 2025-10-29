package com.mllfjn.simyys.interactive;

public class Info {
    private final TraceableNumber traceableNumber = new TraceableNumber();
    private boolean baoJi;

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
