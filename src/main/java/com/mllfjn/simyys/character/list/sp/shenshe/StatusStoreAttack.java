package com.mllfjn.simyys.character.list.sp.shenshe;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.AttributeModifier;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;

// 这是神蛇被吸攻击的队友身上的状态
public class StatusStoreAttack extends Status implements AttributeModifier {
    private int stack = 1;

    public StatusStoreAttack(Character from, Character belongTo) {
        // from为神蛇, belongTo为被吸攻击的队友
        super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
    }

    public static void addStack(Character from, Character character) {
        character.getStatus(StatusStoreAttack.class)
                .or(() -> character.addStatus(new StatusStoreAttack(from, character)))
                .ifPresent(StatusStoreAttack::addStack);
    }

    public void addStack() {
        stack++;
    }

    @Override
    public boolean isAffectAttribute(Attribute attribute) {
        return attribute == Attribute.ATTACK;
    }

    @Override
    public double getInfluence(Attribute attribute, StatusModifyParam param) {
        // 每被吸一次减少6%
        return belongTo.getInitAttack() * -0.06 * stack;
    }
}
