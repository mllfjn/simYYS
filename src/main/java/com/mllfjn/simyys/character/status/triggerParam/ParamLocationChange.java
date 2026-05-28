package com.mllfjn.simyys.character.status.triggerParam;

public class ParamLocationChange extends TriggerParam {
    public final double oldLocation;
    public final double newLocation;
    public final boolean isFromIncrease;

    private boolean isCanceled;

    public ParamLocationChange(double oldLocation, double newLocation, boolean isFromIncrease) {
        this.oldLocation = oldLocation;
        this.newLocation = newLocation;
        this.isFromIncrease = isFromIncrease;
    }

    public void cancel() {
        isCanceled = true;
    }

    public boolean isCanceled() {
        return isCanceled;
    }
}
