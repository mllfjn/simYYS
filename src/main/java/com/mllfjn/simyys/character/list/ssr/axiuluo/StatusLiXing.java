package com.mllfjn.simyys.character.list.ssr.axiuluo;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.determinant.InfluenceDamageWhenAttack;
import com.mllfjn.simyys.character.status.determinant.RetainAfterChangeWave;
import com.mllfjn.simyys.character.status.determinant.RetainAfterDie;
import com.mllfjn.simyys.character.status.triggerParam.ParamAddCrowdControl;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackInfo;

class StatusLiXing extends Status
        implements Displayable, RetainAfterDie, RetainAfterChangeWave, InfluenceDamageWhenAttack, StatusRunnable {
    private static final String StatusName = "理性";

    private final double suppressPerStack;
    private final boolean reduceDamage;
    private int stack;

    public StatusLiXing(Character character, double suppressPerStack, boolean reduceDamage) {
        super(character, character, StatusType.GENERAL, StatusForm.YIN_JI);
        this.suppressPerStack = suppressPerStack;
        this.reduceDamage = reduceDamage;

        character.bp.addPriorityMove(character, () -> stack = 9);
    }

    void consume(int useStack) {
        stack -= useStack;
    }

    int getStack() {
        return stack;
    }

    @Override
    public String getDisplayText() {
        return StatusName + stack;
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
    public boolean runnable(Trigger trigger) {
        return stack < 9 && ( // 理性小于9是前提
                (reduceDamage && trigger == Trigger.BEING_ATTACKED) // 减伤
                        || (trigger == Trigger.ADDING_CROWD_CONTROL) // 免控
        );
    }

    @Override
    public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
        if (trigger == Trigger.BEING_ATTACKED) {
            ((ParamAttackInfo) param).getAttackInfo().getTraceableNumber()
                    .mul(1 - 0.06 * (9 - stack), StatusName);
        } else {
            ((ParamAddCrowdControl) param).getEffectInfo().setCancel(true);
        }
        return false;
    }
}
