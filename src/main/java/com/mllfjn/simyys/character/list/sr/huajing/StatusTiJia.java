package com.mllfjn.simyys.character.list.sr.huajing;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackInfo;

class StatusTiJia extends Status implements Displayable, StatusRunnable {
    private int stack = 3;
    private final Skill3 skill3;

    public StatusTiJia(Character from, Character belongTo, Skill3 skill3) {
        super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
        this.skill3 = skill3;
    }

    @Override
    public void beforeDelete() {
        ((HuaJing) from).statusTiJia = null;
    }

    @Override
    public String getDisplayText() {
        return Skill3.SkillName + stack;
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return trigger == Trigger.BEFORE_ROUND
                || trigger == Trigger.BEFORE_ATTACK
                || trigger == Trigger.AFTER_ROUND && ((HuaJing) belongTo).tiJiaIncrease();
    }

    @Override
    public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
        if (trigger == Trigger.BEFORE_ROUND) {
            from.doInteractive(interactive ->
                    interactive.healTypical(skill3, belongTo, skill3.getMultiplier()))
            ;
            skill3.useDone();
        } else {
            AttackInfo attackInfo = ((ParamAttackInfo) param).getAttackInfo();
            if (attackInfo.isCrit()) {
                attackInfo.getTraceableNumber().mul(0.5, Skill3.SkillName);
                if (stack == 1) {
                    return true;
                } else {
                    stack--;
                }
            }
        }
        return false;
    }
}
