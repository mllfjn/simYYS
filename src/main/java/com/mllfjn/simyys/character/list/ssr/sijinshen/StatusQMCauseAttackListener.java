package com.mllfjn.simyys.character.list.ssr.sijinshen;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.ParamUseSkill;

class StatusQMCauseAttackListener extends Status {
    private int count;

    public StatusQMCauseAttackListener(SkillQiMeng skillQiMeng, Character from, Character belongTo) {
        super(SkillQiMeng.SkillName + "造成伤害监听", from, belongTo);
        runOnAndDisable(Trigger.USED_SKILL, _ -> {
            disableAction(Trigger.USED_SKILL);
            disableAction(Trigger.CAUSE_ATTACK);
            count = 0;
            skillQiMeng.useDone();
        });
        runOnAndDisable(Trigger.CAUSE_ATTACK, param -> {
            count++;
            if (count == 1) {
                skillQiMeng.log();
            }
            skillQiMeng.addAttackInfo(((ParamAttackInfo) param).getAttackInfo());

            if (count == 40) {
                disableAction(Trigger.CAUSE_ATTACK);
            }
        });
        runOn(Trigger.WILL_USE_SKILL, param -> {
            if (skillQiMeng.isEffective()) {
                enableAction(Trigger.USED_SKILL);
                enableAction(Trigger.CAUSE_ATTACK);
                skillQiMeng.start(((ParamUseSkill) param).getSkill());
            }
        });
    }
}
