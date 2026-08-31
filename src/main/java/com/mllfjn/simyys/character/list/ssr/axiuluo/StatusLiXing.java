package com.mllfjn.simyys.character.list.ssr.axiuluo;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAddCrowdControl;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackInfo;

class StatusLiXing extends Status {
    private static final String StatusName = "理性";

    private final double suppressPerStack;
    private int stack = 9;

    public StatusLiXing(Character character, double suppressPerStack, boolean reduceDamage) {
        super(StatusName, character, character, StatusType.GENERAL, StatusForm.YIN_JI);
        this.suppressPerStack = suppressPerStack;
        display(() -> StatusName + stack);
        retainAfterDie();
        retainAfterChangeWave(() -> stack = 9);
        // 减伤
        if (reduceDamage) {
            runOn(Trigger.BEING_ATTACKED, param ->
                    ((ParamAttackInfo) param).getAttackInfo().getTraceableNumber().mul(1 - 0.06 * (9 - stack),
                            StatusName)
            );
        }
        // 免控
        runOn(Trigger.ADDING_CROWD_CONTROL, param ->
                ((ParamAddCrowdControl) param).getEffectInfo().setCancel(true)
        );
    }

    void consume(int useStack) {
        stack -= useStack;
    }

    int getStack() {
        return stack;
    }

    @Override
    public void doInfluenceWhenAttack(AttackInfo attackInfo) {
        if (stack > 0) {
            attackInfo.getTraceableNumber().mul((1 - stack * suppressPerStack), StatusName);
        }

        if (belongTo.getInitSpeed() > attackInfo.getTarget().getInitSpeed()) {
            attackInfo.getTraceableNumber().mul(1.1, AXiuLuo.CharacterName + "速度快于目标");
        }
    }

    @Override
    public final boolean runnable(Trigger trigger) {
        if (stack < 9) {
            return super.runnable(trigger);
        } else {
            return false;
        }
    }
}
