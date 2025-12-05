package com.mllfjn.simyys.character.list.sp.shenshe;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.AttributeModifier;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;

public class StatusAddAttack extends Status implements AttributeModifier {
    private double attack;

    public StatusAddAttack(Character character) {
        super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
    }

    public static void addAttack(ShenShe shenShe, double attack) {
        shenShe.getStatus(StatusAddAttack.class)
                .or(() -> shenShe.addStatus(new StatusAddAttack(shenShe)))
                .ifPresent(status -> status.addAttack(attack));
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
