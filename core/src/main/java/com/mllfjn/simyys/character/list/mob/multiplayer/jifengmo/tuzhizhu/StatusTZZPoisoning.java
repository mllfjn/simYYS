package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.tuzhizhu;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.interactive.AttackType;

/**
 * 土蜘蛛的中毒和一般中毒不是一回事，效果是每回合行动前造成土蜘蛛攻击10%的伤害，最多叠加3层
 */
class StatusTZZPoisoning extends Status {
    private static final String StatusName = "蜘蛛毒";
    private static final Skill SKILL = Skill.getInstance(StatusName);

    private int stack = 0;

    private StatusTZZPoisoning(Character from, Character belongTo, int duration) {
        super(StatusName, from, belongTo);
        type(StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
        duration(StatusDurationType.CHI_XU, duration);
        runOn(Trigger.BEFORE_ROUND, _ ->
                from.doInteractive(interactive -> {
                    for (int i = 0; i < stack; i++) {
                        interactive.attackTypical(SKILL, belongTo, 10, AttackType.DAN_TI);
                    }
                    SKILL.useDone();
                })
        );
    }

    static void addTZZPoisoning(Character from, Character belongTo, int duration) {
        int totalStack = 0;
        StatusTZZPoisoning StatusCurrentDuration = null;
        for (Status status : belongTo.getStatuses()) {
            if (status instanceof StatusTZZPoisoning stp) {
                totalStack += stp.stack;
                if (totalStack == 3) {
                    return;
                }
                if (stp.getDuration() == duration) {
                    StatusCurrentDuration = stp;
                }
            }
        }
        if (StatusCurrentDuration == null) {
            StatusCurrentDuration = new StatusTZZPoisoning(from, belongTo, duration);
            belongTo.addStatus(StatusCurrentDuration);
        }
        StatusCurrentDuration.stack++;
    }
}
