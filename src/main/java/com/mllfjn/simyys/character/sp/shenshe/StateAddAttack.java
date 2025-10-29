package com.mllfjn.simyys.character.sp.shenshe;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.state.AttributeModifier;
import com.mllfjn.simyys.state.State;
import com.mllfjn.simyys.state.StateForm;
import com.mllfjn.simyys.state.StateType;

public class StateAddAttack extends State implements AttributeModifier {
    public static final String privateName = "天羽羽斩增加攻击";
    private double attack;

    public StateAddAttack(Character character) {
        super(character, character, StateType.SPECIAL, StateForm.SPECIAL);
    }

    public static void addAttack(ShenShe shenShe, double attack) {
        StateAddAttack state = (StateAddAttack) shenShe.getState(privateName);
        if (state == null) {
            state = new StateAddAttack(shenShe);
            shenShe.addState(state);
        }

        state.addAttack(attack);
    }

    private void addAttack(double attack) {
        this.attack += attack;
    }

    @Override
    public void setName() {
        name = privateName;
    }

    @Override
    public boolean isAffectAttribute(Attribute attribute) {
        return attribute == Attribute.ATTACK;
    }

    @Override
    public double getInfluence(Attribute attribute) {
        // 总值不超过自身初始攻击100%
        return Math.min(attack, belongTo.getInitAttack());
    }
}
