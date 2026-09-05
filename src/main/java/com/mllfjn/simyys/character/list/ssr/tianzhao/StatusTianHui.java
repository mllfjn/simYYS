package com.mllfjn.simyys.character.list.ssr.tianzhao;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.ParamUseSkill;
import com.mllfjn.simyys.interactive.AttackInfo;

class StatusTianHui extends Status {
    private static final String StatusName = "天辉";

    private Skill copySkill;

    StatusTianHui(Character from, Character belongTo, Skill2 skill2, Skill3 skill3) {
        super(StatusName, from, belongTo, StatusType.GENERAL, StatusForm.YIN_JI);
        skill3.statusTianHui = this;

        preventDie(_ -> from.lostHP(1));

        duration(StatusDurationType.WEI_CHI, 2);
        beforeDelete(() -> skill3.statusTianHui = null);
        attribute(Attribute.ATTACK, -0.15 * belongTo.getInitAttack());
        displayName();

        runOnAndDisable(Trigger.CAUSE_ATTACK, param -> {
            AttackInfo attackInfo = ((ParamAttackInfo) param).getAttackInfo();
            if (attackInfo.getSkill() == copySkill) {
                skill2.causeAttack(attackInfo);
            }
        });
        // 在自身回合释放普攻或技能
        runOn(Trigger.WILL_USE_PU_GONG, Trigger.WILL_USE_SKILL, param -> {
            if (belongTo.isInRound()) {
                copySkill = ((ParamUseSkill) param).getSkill();
                enableAction(Trigger.CAUSE_ATTACK);
                copySkill.addSkillEndListener(() -> {
                    copySkill = null;
                    skill2.copyDone(belongTo);
                    disableAction(Trigger.CAUSE_ATTACK);
                });
            }
        });
    }
}
