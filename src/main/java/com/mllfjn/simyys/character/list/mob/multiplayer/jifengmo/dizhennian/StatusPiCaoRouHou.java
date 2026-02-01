package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.dizhennian;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.EventBattleStart;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.determinant.InfluenceDamageBeingAttack;
import com.mllfjn.simyys.character.status.triggerParam.ParamAfterAttack;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackInfo;

class StatusPiCaoRouHou extends Status implements StatusRunnable, Displayable {
    public static final String StatusName = "皮糙肉厚";

    private final double breakDamage;

    private int stack = 10;

    public StatusPiCaoRouHou(Character character, double breakDamage) {
        super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        this.breakDamage = breakDamage;

        belongTo.bp.addActionListener(belongTo, event -> {
            if (event instanceof EventBattleStart) {
                for (Character target : belongTo.bp.situation.characters) {
                    target.addStatus(new StatusPiCaoRouHouReduceDamage(character, target));
                }
                return true;
            }
            return false;
        });


    }

    @Override
    public String getDisplayText() {
        return StatusName + stack;
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return trigger == Trigger.AFTER_ATTACK;
    }

    @Override
    public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
        if (param instanceof ParamAfterAttack paa) {
            double number = paa.attackInfo.getTraceableNumber().getNumber();
            if (number > breakDamage) {
                stack--;
                return stack == 0;
            }
        }
        return false;
    }

    @Override
    public void beforeDelete() {
        for (Character character : belongTo.bp.situation.characters) {
            character.removeStatus(StatusPiCaoRouHouReduceDamage.class);
        }
    }

    static class StatusPiCaoRouHouReduceDamage extends Status implements InfluenceDamageBeingAttack {
        public StatusPiCaoRouHouReduceDamage(Character from, Character belongTo) {
            super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public void doInfluenceBeingAttack(AttackInfo attackInfo) {
            attackInfo.getTraceableNumber().mul(0.3, StatusPiCaoRouHou.StatusName);
        }
    }
}
