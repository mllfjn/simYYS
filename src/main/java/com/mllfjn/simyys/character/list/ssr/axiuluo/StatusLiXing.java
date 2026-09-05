package com.mllfjn.simyys.character.list.ssr.axiuluo;

import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAddCrowdControl;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.interactive.AttackInfo;

class StatusLiXing extends Status {
    private static final String StatusName = "理性";

    private int stack = 9;

    public StatusLiXing(AXiuLuo character, double suppressPerStack, boolean reduceDamage) {
        super(StatusName, character, character, StatusType.GENERAL, StatusForm.YIN_JI);
        display(() -> StatusName + stack);
        retainAfterDie();
        retainAfterChangeWave(() -> stack = 9);
        // 减伤
        if (reduceDamage) {
            runOnAndDisable(Trigger.BEING_ATTACKED, param ->
                    ((ParamAttackInfo) param).getAttackInfo().getTraceableNumber().mul(1 - 0.06 * (9 - stack),
                            StatusName)
            );
        }
        // 免控
        runOnAndDisable(Trigger.ADDING_CROWD_CONTROL, param ->
                ((ParamAddCrowdControl) param).getEffectInfo().setCancel(true)
        );
        // 回合开始时回复满
        runOnAndDisable(Trigger.BEFORE_ROUND, _ -> refuel());
        runOn(Trigger.WHEN_ATTACK, param -> {
            AttackInfo attackInfo = ((ParamAttackInfo) param).getAttackInfo();
            if (stack > 0) {
                attackInfo.getTraceableNumber().mul((1 - stack * suppressPerStack), StatusName);
            }

            if (belongTo.getInitSpeed() > attackInfo.getTarget().getInitSpeed()) {
                attackInfo.getTraceableNumber().mul(1.1, AXiuLuo.CharacterName + "速度快于目标");
            }
        });
    }

    void consume(int useStack) {
        if (stack == 9) {
            enableAction(Trigger.BEING_ATTACKED);
            enableAction(Trigger.ADDING_CROWD_CONTROL);
            enableAction(Trigger.BEFORE_ROUND);
        }
        stack -= useStack;
    }

    int getStack() {
        return stack;
    }

    private void refuel() {
        if (stack < 9) {
            stack = 9;
            disableAction(Trigger.BEING_ATTACKED);
            disableAction(Trigger.ADDING_CROWD_CONTROL);
            disableAction(Trigger.BEFORE_ROUND);
        }
    }
}
