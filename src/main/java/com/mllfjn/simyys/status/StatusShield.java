package com.mllfjn.simyys.status;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.interactive.Info;
import com.mllfjn.simyys.interactive.TraceableNumber;

public class StatusShield extends Status implements Displayable {
    public static final String StatusName = "护盾";
    private double shield;

    public StatusShield(Character from, Character belongTo, double shield) {
        super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
        this.shield = shield;
    }

    @Override
    public String getText() {
        return StatusName;
    }

    /**
     *
     * @param info 伤害信息
     * @return return true if shield run out
     */
    public boolean handle(Info info) {
        TraceableNumber traceableNumber = info.getTraceableNumber();
        double number = traceableNumber.getNumber();
        if (number > shield) {
            traceableNumber.sub(shield, StatusName);
            return true;
        } else {
            shield -= number;
            traceableNumber.sub(number, StatusName);
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
