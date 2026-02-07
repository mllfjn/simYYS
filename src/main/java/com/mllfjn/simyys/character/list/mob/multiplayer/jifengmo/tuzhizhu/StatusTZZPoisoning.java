package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.tuzhizhu;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackType;

/**
 * 土蜘蛛的中毒和一般中毒不是一回事，效果是每回合行动前造成土蜘蛛攻击10%的伤害，最多叠加3层
 */
class StatusTZZPoisoning extends Status implements StatusRunnable {
    private static final String StatusName = "蜘蛛毒";
    private static final Skill SKILL = Skill.getInstance(StatusName);

    private int stack = 0;

    private StatusTZZPoisoning(Character from, Character belongTo, int duration) {
        super(from, belongTo, StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
        setDurationType(StatusDurationType.CHI_XU, duration);
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

    @Override
    public boolean runnable(Trigger trigger) {
        return trigger == Trigger.BEFORE_ROUND;
    }

    @Override
    public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
        from.doInteractive(interactive -> {
            for (int i = 0; i < stack; i++) {
                interactive.attackTypical(SKILL, belongTo, 10, AttackType.DAN_TI);
            }
            SKILL.useDone();
        });
        return false;
    }
}
