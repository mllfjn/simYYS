package com.mllfjn.simyys.character.list.ssr.xunxiangxing;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.BattleActionListener;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.determinant.InfluenceDamageWhenAttack;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackInfo;

class StatusHuanJing extends Status implements Displayable, InfluenceDamageWhenAttack {
    private static final String StatusName = "明香境";

    private final Skill2 skill2;
    private final BattleActionListener listener;

    private StatusHuanJing(Skill2 skill2, Character character) {
        super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        this.skill2 = skill2;
        setDurationType(StatusDurationType.WEI_CHI, 3);
        listener = character.bp.forEveryone(character, c -> {
            // 除自身外非召唤物友方
            if (c.team == character.team && !c.isSummon() && c != character) {
                c.addStatus(new StatusAfterRound(character, c));
            }
        });
    }

    @Override
    public void beforeDelete() {
        belongTo.bp.removeActionListener(belongTo, listener);
        for (Character c : belongTo.bp.situation.characters) {
            if (c.team == belongTo.team && !c.isSummon() && c != belongTo) {
                c.removeStatus(StatusAfterRound.class);
            }
        }
    }

    void usedSkill3() {
        if (skill2.getLevel() >= 2) {
            setDuration(getDuration() + 1);
        }
    }

    static void install(Skill2 skill2, Character character) {
        character.getStatus(StatusHuanJing.class)
                .ifPresentOrElse(
                        status -> status.setDuration(3),
                        () -> character.addStatus(new StatusHuanJing(skill2, character))
                );
    }

    @Override
    public String getDisplayText() {
        return StatusName + getDuration();
    }

    @Override
    public void doInfluenceWhenAttack(AttackInfo attackInfo) {
        Character target = attackInfo.getTarget();
        if (target.team != belongTo.team) {
            double targetDefence = target.getDefence();
            double xxxDefence = belongTo.getDefence();
            if (xxxDefence > targetDefence) {
                // 敌方防御每比寻香行低1%,寻香行造成的伤害提升1%(以1%为最小单位)
                int percent = (int) ((xxxDefence - targetDefence) / xxxDefence * 100);
                attackInfo.getTraceableNumber().mul(1 + (0.01 * percent), StatusName);
            }
            // 由于是攻击时,interactive一定是belongTo的
            belongTo.bp.interactive.
                    effect(skill2, target, 40, true, StatusFuHunXiang.getSupplier());
        }
    }

    // 该状态在友方身上，回合结束后寻香行获得1层心香
    static class StatusAfterRound extends Status implements StatusRunnable {
        public StatusAfterRound(Character from, Character belongTo) {
            super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.AFTER_ROUND;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            Skill2.StatusXinXiang.addStack(from);
            return false;
        }
    }
}
