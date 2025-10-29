package com.mllfjn.simyys.character.ssr.namei;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.state.*;
import com.mllfjn.simyys.state.Runnable;
import com.mllfjn.simyys.trigger.Trigger;

public class StateHuiMieTODO extends State implements Displayable, Runnable, AttributeModifier {
    public static final String privateName = "毁灭";
    private int stack = 1;
    private final int level;

    public StateHuiMieTODO(Character from, Character to, int level) {
        super(from, to, StateType.BUFF, StateForm.YIN_JI);
        this.level = level;
    }

    @Override
    public void setName() {
        name = privateName;
    }

    @Override
    public String getText() {
        return "毁灭" + stack;
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return stack < 6 && trigger == Trigger.BEFORE_ROUND;
    }

    @Override
    public void run(Trigger trigger, BattlePane bp) {
        stack++;
    }

    @Override
    public boolean isAffectAttribute(Attribute attribute) {
        return attribute == Attribute.DEFENCE || attribute == Attribute.IGNORE_DEFENCE;
    }

    @Override
    public double getInfluence(Attribute attribute) {
        if (attribute == Attribute.DEFENCE) {
            return (1 - stack) * 100; // 降低防御(stack - 1) * 100，这里算的是+防御，所以负的
        } else if (attribute == Attribute.IGNORE_DEFENCE) {
            return 100 + level >= 4 ? (stack - 1) * 40 : 0;
        }

        return 0;
    }
}
