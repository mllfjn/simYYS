package com.mllfjn.simyys.character.list.sp.fuji;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackInfo;

class StatusYuanHuo extends Status implements StatusRunnable {
    private static final String StatusName = "怨火";
    private static final Skill SKILL = Skill.getInstance(StatusName);

    public StatusYuanHuo(Character from, Character belongTo) {
        super(from, belongTo, StatusType.DEBUFF, StatusForm.YIN_JI);
        setDurationType(StatusDurationType.CHI_XU, 1);
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return trigger == Trigger.BEFORE_ROUND;
    }

    @Override
    public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
        from.doInteractive(interactive -> {
            AttackInfo attackInfo = AttackInfo
                    .createJianJieAttack(from, SKILL, belongTo, from.getAttack());
            attackInfo.setMultiplier(99);
            interactive.attack(attackInfo);
            SKILL.useDone();
        });
        return false;
    }
}
