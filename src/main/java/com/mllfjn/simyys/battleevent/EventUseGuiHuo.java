package com.mllfjn.simyys.battleevent;

public class EventUseGuiHuo extends BattleEvent {
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
