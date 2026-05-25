package com.mllfjn.simyys.character.list.ssr.dishitian;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackInfo;

abstract class StatusJinLian extends Status implements Displayable {
    private static final String StatusName = "金莲";

    protected StatusJinLian(com.mllfjn.simyys.character.Character from, Character belongTo, StatusType statusType, StatusForm statusForm) {
        super(from, belongTo, statusType, statusForm);
    }

    @Override
    public void beforeDelete() {
        ((DiShiTian) from).removeJinLian();
    }

    @Override
    public String getDisplayText() {
        return StatusName;
    }
}

class StatusJinLianForMob extends StatusJinLian implements StatusRunnable {
    private final boolean extraDamage;
    private final Skill2 skill2;

    public StatusJinLianForMob(Character from, Character belongTo, boolean extraDamage, Skill2 skill2) {
        super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
        this.extraDamage = extraDamage;
        this.skill2 = skill2;
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return trigger == Trigger.AFTER_ATTACK;
    }

    @Override
    public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
        if (param instanceof ParamAttackInfo pai && pai.getAttackInfo().getSkill() != skill2) {
            double number = pai.getAttackInfo().getTraceableNumber().getNumber() * 0.4;
            if (extraDamage) {
                number += from.getAttack() * 0.7;
            }
            double finalNumber = number;
            AttackInfo attackInfo = AttackInfo
                    .createRealAttack(from, skill2, belongTo, (c1, c2) -> finalNumber);
            attackInfo.setCalYuHun(false);
            from.doInteractive(interactive -> interactive.attack(attackInfo));
        }
        return false;
    }
}

class StatusJinLianNormal extends StatusJinLian {
    public StatusJinLianNormal(Character from, Character belongTo) {
        super(from, belongTo, StatusType.DEBUFF, StatusForm.YIN_JI);
    }
}