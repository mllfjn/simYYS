package com.mllfjn.simyys.character.list.sr.huajing;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.interactive.AttackInfo;

class StatusTiJia extends Status {
    public StatusTiJia(HuaJing from, Character belongTo, Skill3 skill3) {
        super(Skill3.SkillName, from, belongTo);
        beforeDelete(() -> from.statusTiJia = null);
        enableStack(3);
        setStack(3);
        displayNameAndStack();
        runOn(Trigger.BEFORE_ROUND, _ -> {
            from.doInteractive(interactive ->
                    interactive.healTypical(skill3, belongTo, skill3.getMultiplier()))
            ;
            skill3.useDone();
        });
        runOn(Trigger.BEFORE_ATTACK, param -> {
            AttackInfo attackInfo = ((ParamAttackInfo) param).getAttackInfo();
            if (attackInfo.isCrit()) {
                attackInfo.getTraceableNumber().mul(0.5, Skill3.SkillName);
                reduceStack();
            }
        });
        runOn(Trigger.AFTER_ROUND, _ -> {
            if (from.tiJiaIncrease()) {
                from.doInteractive(interactive -> interactive.increaseLocation(belongTo, 20));
            }
        });
    }

    @Override
    public void reduceStack() {
        super.reduceStack();
        if (stack == 0) {
            delete();
        }
    }
}
