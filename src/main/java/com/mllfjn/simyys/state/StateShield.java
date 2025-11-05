package com.mllfjn.simyys.state;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.interactive.Info;
import com.mllfjn.simyys.interactive.TraceableNumber;

public class StateShield extends State implements Displayable {
    public static final String StateName = "护盾";
    private double shield;

    public StateShield(Character from, Character belongTo, double shield) {
        super(from, belongTo, StateType.SPECIAL, StateForm.SPECIAL);
        this.shield = shield;
    }

    @Override
    public String getText() {
        return StateName;
    }

    /**
     *
     * @param info 伤害
     * @return return true if shield run out
     */
    public boolean handle(Info info) {
        TraceableNumber traceableNumber = info.getTraceableNumber();
        double number = traceableNumber.getNumber();
        if (number > shield) {
            traceableNumber.sub(shield, StateName);
            return true;
        } else {
            shield -= number;
            traceableNumber.sub(number, StateName);
            return false;
        }
    }

    public double getShield() {
        return shield;
    }
}
