package com.mllfjn.simyys.character.sp.dayuan;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.state.*;

class StateShenLi extends State implements AttributeModifier, Displayable {
    public static final String privateName = "神力";
    private int stack;

    public StateShenLi(Character character) {
        super(character, character, StateType.BUFF, StateForm.YIN_JI);
    }

    public static void addStack(Character character, int count) {
        StateShenLi shenLi = (StateShenLi) character.getState(privateName);
        if (shenLi == null) {
            shenLi = new StateShenLi(character);
            character.addState(shenLi);
        }
        shenLi.stack += count;
        if (shenLi.stack > 5) {
            shenLi.stack = 5;
        }
    }

    public int getStack() {
        return stack;
    }

    @Override
    public void setName() {
        this.name = privateName;
    }

    @Override
    public boolean isAffectAttribute(Attribute attribute) {
        return attribute == Attribute.EFFECT_RESIST_RATE;
    }

    @Override
    public double getInfluence(Attribute attribute) {
        return attribute == Attribute.EFFECT_RESIST_RATE ? stack * 20 : 0;
    }

    @Override
    public String getText() {
        return "神力" + stack;
    }
}
