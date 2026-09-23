package com.mllfjn.simyys.character.list.ssr.xunxiangxing;

import com.mllfjn.simyys.battleevent.StatusAdder;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.interactive.AttackInfo;

class StatusHuanJing extends Status {
    private static final String StatusName = "明香境";

    private final boolean isAwakening;
    private final Skill2 skill2;

    private StatusHuanJing(Skill2 skill2, Character character) {
        super(StatusName, character);
        this.skill2 = skill2;
        duration(StatusDurationType.WEI_CHI, 3);
        displayNameAndDuration();
        // 除自身外非召唤物友方
        StatusAdder<?> adder = character.bp.addStatusAdder(c ->
                // 除自身外非召唤物友方
                c.team == character.team && c != character && !c.isSummon()
                        ? new StatusAfterRound(character, c)
                        : null
        );
        beforeDelete(adder::deleteAndRemove);
        isAwakening = ((XunXiangXing) character).awakening;
        runOn(Trigger.WHEN_ATTACK, triggerParam -> {
            AttackInfo attackInfo = ((ParamAttackInfo) triggerParam).getAttackInfo();
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
                if (isAwakening && attackInfo.getSkill() instanceof Skill3) {
                    StatusFuHunXiang.addStack(belongTo, target);
                } else {
                    belongTo.bp.interactive.
                            effect(skill2, target, 40, true, StatusFuHunXiang.getSupplier());
                }
            }
        });
    }

    void usedSkill3() {
        if (skill2.getLevel() >= 2) {
            duration(getDuration() + 1);
        }
    }

    static void install(Skill2 skill2, Character character) {
        character.getStatus(StatusHuanJing.class)
                .ifPresentOrElse(
                        status -> status.duration(3),
                        () -> character.addStatus(new StatusHuanJing(skill2, character))
                );
    }

    // 该状态在友方身上，回合结束后寻香行获得1层心香
    class StatusAfterRound extends Status {
        public StatusAfterRound(Character from, Character belongTo) {
            super("回合结束获得心香", from, belongTo);
            runOn(Trigger.AFTER_ROUND, _ ->
                    Skill2.StatusXinXiang.addStack(from, StatusHuanJing.this.skill2)
            );
        }
    }
}
