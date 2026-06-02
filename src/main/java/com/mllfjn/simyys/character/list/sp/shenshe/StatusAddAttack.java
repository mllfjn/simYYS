package com.mllfjn.simyys.character.list.sp.shenshe;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.AttributeModifier;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;
import com.mllfjn.simyys.character.status.determinant.RetainAfterChangeWave;

class StatusAddAttack extends Status implements AttributeModifier, RetainAfterChangeWave {
    private double attack;

    public StatusAddAttack(Character character) {
        super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
    }

    void addAttack(double attack) {
        this.attack += attack;
    }

    @Override
    public boolean isAffectAttribute(Attribute attribute) {
        return attribute == Attribute.ATTACK;
    }

    @Override
    public double getInfluence(Attribute attribute, StatusModifyParam param) {
        // 总值不超过自身初始攻击100%
        return Math.min(attack, belongTo.getInitAttack());
    }
}
