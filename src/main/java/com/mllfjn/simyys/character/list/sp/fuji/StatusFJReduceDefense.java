package com.mllfjn.simyys.character.list.sp.fuji;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;

class StatusFJReduceDefense extends Status implements AttributeModifier, Displayable {
    private static final String StatusName = "缚姬减防";

    private final int defensePerStack;
    private int stack = 1;

    private StatusFJReduceDefense(Character from, Character belongTo, int defensePerStack) {
        super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
        this.defensePerStack = defensePerStack;
    }

    public static void addStack(Character from, Character belongTo, int defensePerStack) {
        belongTo.getStatus(StatusFJReduceDefense.class)
                .ifPresentOrElse(
                        StatusFJReduceDefense::addStack,
                        () -> belongTo.addStatus(new StatusFJReduceDefense(from, belongTo, defensePerStack))
                );
    }

    private void addStack() {
        if (stack < 10) {
            stack++;
        }
    }

    @Override
    public boolean isAffectAttribute(Attribute attribute) {
        return attribute == Attribute.DEFENCE;
    }

    @Override
    public double getInfluence(Attribute attribute) {
        return -defensePerStack * stack;
    }

    @Override
    public String getDisplayText() {
        return StatusName + stack;
    }
}
