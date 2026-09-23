package com.mllfjn.simyys.character.status.triggerParam;

import com.mllfjn.simyys.character.Character;

public class ParamLocationChange extends TriggerParam {
    public final double oldLocation;
    public final double newLocation;

    public final Character from;
    public final boolean isFromIncrease;
    public final boolean isFromDecrease;

    private boolean isCanceled;

    private ParamLocationChange(double oldLocation, double newLocation, Character from, boolean isFromIncrease, boolean isFromDecrease) {
        this.oldLocation = oldLocation;
        this.newLocation = newLocation;
        this.from = from;
        this.isFromIncrease = isFromIncrease;
        this.isFromDecrease = isFromDecrease;
    }

    public static ParamLocationChange increase(double oldLocation, double newLocation, Character from) {
        return new ParamLocationChange(oldLocation, newLocation, from, true, false);
    }

    public static ParamLocationChange decrease(double oldLocation, double newLocation, Character from) {
        return new ParamLocationChange(oldLocation, newLocation, from, false, true);
    }

    public static ParamLocationChange normal(double oldLocation, double newLocation) {
        return new ParamLocationChange(oldLocation, newLocation, null, false, false);
    }

    public void cancel() {
        isCanceled = true;
    }

    public boolean isCanceled() {
        return isCanceled;
    }
}
