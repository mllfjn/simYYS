package com.mllfjn.simyys.character.list.ssr.sijinshen;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;

class StatusQMCauseAttackListener extends Status implements StatusRunnable {
    private final SkillQiMeng skillQiMeng;
    private boolean start = false;
    private int count;

    public StatusQMCauseAttackListener(SkillQiMeng skillQiMeng, Character from, Character belongTo) {
        super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
        this.skillQiMeng = skillQiMeng;
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return trigger == Trigger.WILL_USE_SKILL
                || trigger == Trigger.USED_SKILL
                || (start && count <= 40 && trigger == Trigger.CAUSE_ATTACK && skillQiMeng.isEffective());
    }

    @Override
    public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
        if (trigger == Trigger.WILL_USE_SKILL) {
            start = true;
        } else if (trigger == Trigger.USED_SKILL) {
            start = false;
            count = 0;
        } else {
            skillQiMeng.doInteractive(((ParamAttackInfo) param).getAttackInfo());
            count++;
        }
        return false;
    }
}
