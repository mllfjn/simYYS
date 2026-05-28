package com.mllfjn.simyys.character.list.sp.sphongye;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.instance.StatusPoisoning;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.AttackType;

class StatusYeJin extends Status implements Displayable, StatusRunnable {
    private static final String StatusName = "叶烬";

    private final Skill2 skill2;

    private int stack = 0;

    private StatusYeJin(Character from, Character belongTo, Skill2 skill2) {
        super(from, belongTo, StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
        this.skill2 = skill2;

        addStack();
    }

    static void addStack(Character from, Character belongTo, Skill2 skill2) {
        belongTo.getStatus(StatusYeJin.class)
                .ifPresentOrElse(
                        StatusYeJin::addStack,
                        () -> belongTo.addStatus(new StatusYeJin(from, belongTo, skill2))
                );
    }

    private void addStack() {
        if (stack < 5) {
            stack++;
            StatusPoisoning.add(from, belongTo, 3, 2);
        }
    }

    int getStack() {
        return stack;
    }

    @Override
    public String getDisplayText() {
        return StatusName + stack;
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return trigger == Trigger.BEFORE_ROUND // 回合前受到伤害
                || trigger == Trigger.BEING_ATTACKED // 提升受到的间接伤害
                || trigger == Trigger.AFTER_ROUND; // 回合结束减少1层
    }

    @Override
    public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
        if (trigger == Trigger.BEFORE_ROUND) {
            from.doInteractive(interactive -> {
                AttackInfo attackInfo = AttackInfo.createJianJieAttack(from, skill2, belongTo, from.getAttack());
                attackInfo.setMultiplier(10);
                interactive.attack(attackInfo);
                skill2.useDone();
            });
        } else if (trigger == Trigger.BEING_ATTACKED) {
            AttackInfo attackInfo = ((ParamAttackInfo) param).getAttackInfo();
            if (attackInfo.getAttackType() == AttackType.JIAN_JIE) {
                attackInfo.getTraceableNumber().mul(1 + stack * skill2.getDamageIncreasement(), StatusName);
            }
        } else {
            if (stack == 1) {
                return true;
            } else {
                stack--;
                return false;
            }
        }
        return false;
    }
}
