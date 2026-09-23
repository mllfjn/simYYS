package com.mllfjn.simyys.character.list.sp.sphongye;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.instance.StatusPoisoning;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.AttackType;

class StatusYeJin extends Status {
    private static final String StatusName = "叶烬";

    private int stack = 0;

    private StatusYeJin(Character from, Character belongTo, Skill2 skill2) {
        super(StatusName, from, belongTo);
        type(StatusType.DEBUFF, StatusForm.ZHUANG_TAI);

        addStack();

        display(() -> StatusName + stack);
        // 回合前受到伤害
        runOn(Trigger.BEFORE_ROUND, _ ->
                from.doInteractive(interactive -> {
                    AttackInfo attackInfo = AttackInfo.createJianJieAttack(from, skill2, belongTo, from.getAttack());
                    attackInfo.setMultiplier(10);
                    interactive.attack(attackInfo);
                    skill2.useDone();
                })
        );
        // 提升受到的间接伤害
        runOn(Trigger.BEING_ATTACKED, param -> {
            AttackInfo attackInfo = ((ParamAttackInfo) param).getAttackInfo();
            if (attackInfo.getAttackType() == AttackType.JIAN_JIE) {
                attackInfo.getTraceableNumber().mul(1 + stack * skill2.getDamageIncreasement(), StatusName);
            }
        });
        // 回合结束减少1层
        runOn(Trigger.AFTER_ROUND, _ -> {
            if (stack == 1) {
                delete();
            } else {
                stack--;
            }
        });
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
}
