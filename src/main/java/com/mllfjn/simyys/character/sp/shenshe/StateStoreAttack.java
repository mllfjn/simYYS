package com.mllfjn.simyys.character.sp.shenshe;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.state.AttributeModifier;
import com.mllfjn.simyys.state.State;
import com.mllfjn.simyys.state.StateForm;
import com.mllfjn.simyys.state.StateType;

// 这是神蛇被吸攻击的队友身上的状态
public class StateStoreAttack extends State implements AttributeModifier {
    private int stack = 1;

    public StateStoreAttack(Character from, Character belongTo) {
        // from为神蛇, belongTo为被吸攻击的队友
        super(from, belongTo, StateType.SPECIAL, StateForm.SPECIAL);
    }

    public static void addStack(Character from, Character character) {
        character.getState(StateStoreAttack.class)
                .or(() -> character.addState(new StateStoreAttack(from, character)))
                .ifPresent(StateStoreAttack::addStack);
    }

    public void addStack() {
        stack++;
    }

    @Override
    public boolean isAffectAttribute(Attribute attribute) {
        return attribute == Attribute.ATTACK;
    }

    @Override
    public double getInfluence(Attribute attribute) {
        // 每被吸一次减少6%
        return belongTo.getInitAttack() * -0.06 * stack;
    }
}
