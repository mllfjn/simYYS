package com.mllfjn.simyys.trigger;

public class EventUseGuiHuo extends TriggerEvent {
    private final int team;
    private final int num;
    public EventUseGuiHuo(int team, int num) {
        this.team = team;
        this.num = num;
    }

    public int getTeam() {
        return team;
    }

    public int getNum() {
        return num;
    }
}
