package com.mllfjn.simyys.character.status;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.interactive.AttackInfo;
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
     * @param attackInfo 伤害信息
     * @return return true if shield run out or somehow should be removed
     */
    public boolean handle(AttackInfo attackInfo) {
        TraceableNumber traceableNumber = attackInfo.getTraceableNumber();
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
