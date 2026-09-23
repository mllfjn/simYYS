package com.mllfjn.simyys.character.list.sp.shenshe;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Status;

class StatusAddAttack extends Status {
    private double attack;

    public StatusAddAttack(Character character) {
        super("神蛇攻击", character);
        retainAfterChangeWave();
        // 总值不超过自身初始攻击100%
        attribute(Attribute.ATTACK, _ -> Math.min(attack, belongTo.getInitAttack()));
    }

    void addAttack(double attack) {
        this.attack += attack;
    }
}
