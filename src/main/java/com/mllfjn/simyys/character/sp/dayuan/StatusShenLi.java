package com.mllfjn.simyys.character.sp.dayuan;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;

class StatusShenLi extends Status implements AttributeModifier, Displayable {
    // 上限5层,每层提升自身20%效果抵抗,并根据已有层数强化与世结缘(3)效果
    private int stack;

    private StatusShenLi(Character character) {
        super(character, character, StatusType.BUFF, StatusForm.YIN_JI);
        character.addStatus(this);
    }

    public static void addStack(Character character, int count) {
        StatusShenLi shenLi = character.getStatus(StatusShenLi.class).orElseGet(() -> new StatusShenLi(character));
        shenLi.stack += count;
        if (shenLi.stack > 5) {
            shenLi.stack = 5;
        }
    }

    public int getStack() {
        return stack;
    }

    @Override
    public boolean isAffectAttribute(Attribute attribute) {
        return attribute == Attribute.EFFECT_RESIST_RATE;
    }

    @Override
    public double getInfluence(Attribute attribute) {
        return stack * 20;
    }

    @Override
    public String getText() {
        return "神力" + stack;
    }
}
