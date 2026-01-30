package com.mllfjn.simyys.character.status.triggerParam;

public class ParamLocationChange extends TriggerParam {
    public final double oldLocation;
    public final double newLocation;

    public ParamLocationChange(double oldLocation, double newLocation) {
        this.oldLocation = oldLocation;
        this.newLocation = newLocation;
    }
}
