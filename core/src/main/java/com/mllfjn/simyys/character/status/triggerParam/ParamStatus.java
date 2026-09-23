package com.mllfjn.simyys.character.status.triggerParam;

import com.mllfjn.simyys.character.status.Status;

public class ParamStatus extends TriggerParam {
    private final Status status;

    public ParamStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }
}
