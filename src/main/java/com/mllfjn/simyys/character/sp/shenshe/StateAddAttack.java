package com.mllfjn.simyys.character.sp.shenshe;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.state.AttributeModifier;
import com.mllfjn.simyys.state.State;
import com.mllfjn.simyys.state.StateForm;
import com.mllfjn.simyys.state.StateType;

import java.util.Optional;

public class StateAddAttack extends State implements AttributeModifier {
    private double attack;

    public StateAddAttack(Character character) {
        super(character, character, StateType.SPECIAL, StateForm.SPECIAL);
    }

    public static void addAttack(ShenShe shenShe, double attack) {
        shenShe.getState(StateAddAttack.class)
                .or(() -> shenShe.addState(new StateAddAttack(shenShe)))
                .ifPresent(state -> state.addAttack(attack));
    }

    private void addAttack(double attack) {
        this.attack += attack;
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
