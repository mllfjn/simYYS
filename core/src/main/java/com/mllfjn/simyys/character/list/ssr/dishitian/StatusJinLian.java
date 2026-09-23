package com.mllfjn.simyys.character.list.ssr.dishitian;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.interactive.AttackInfo;

abstract class StatusJinLian extends Status {
    private static final String StatusName = "金莲";

    protected StatusJinLian(DiShiTian from, Character belongTo, StatusType statusType, StatusForm statusForm) {
        super(StatusName, from, belongTo, statusType, statusForm);
        beforeDelete(from::removeJinLian);
        displayName();
    }
}

class StatusJinLianForMob extends StatusJinLian {

    public StatusJinLianForMob(DiShiTian from, Character belongTo, boolean extraDamage, Skill2 skill2) {
        super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);

        runOn(Trigger.AFTER_ATTACK, param -> {
            ParamAttackInfo pai = (ParamAttackInfo) param;
            if (pai.getAttackInfo().getSkill() != skill2) {
                double number = pai.getAttackInfo().getTraceableNumber().getNumber() * 0.4;
                if (extraDamage) {
                    number += from.getAttack() * 0.7;
                }
                double finalNumber = number;
                AttackInfo attackInfo = AttackInfo.createRealAttack(from, skill2, belongTo, finalNumber);
                attackInfo.setNotCalYuHun();
                from.doInteractive(interactive -> interactive.attack(attackInfo));
            }
        });
    }
}

class StatusJinLianNormal extends StatusJinLian {
    public StatusJinLianNormal(DiShiTian from, Character belongTo) {
        super(from, belongTo, StatusType.DEBUFF, StatusForm.YIN_JI);
    }
}