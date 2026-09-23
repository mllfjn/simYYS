package com.mllfjn.simyys.character.status.instance;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.interactive.InteractiveInfo;
import com.mllfjn.simyys.interactive.TraceableNumber;

public class StatusShield extends Status {
    private static final String StatusName = "护盾";
    private double shield;

    public StatusShield(Character from, Character belongTo, double shield) {
        super(StatusName, from, belongTo);
        this.shield = shield;
    }

    public StatusShield(String name, Character from, Character belongTo, double shield) {
        super(name, from, belongTo);
        this.shield = shield;
    }

    /**
     *
     * @param interactiveInfo 伤害信息
     * @return return true if shield run out or somehow should be removed
     */
    public boolean handle(InteractiveInfo interactiveInfo) {
        TraceableNumber traceableNumber = interactiveInfo.getTraceableNumber();
        double number = traceableNumber.getNumber();
        if (number > shield) {
            traceableNumber.sub(shield, getName());
            return true;
        } else {
            shield -= number;
            traceableNumber.sub(number, getName());
            return false;
        }
    }

    public double getShield() {
        return shield;
    }

    protected void setShield(double shield) {
        this.shield = shield;
    }
}
