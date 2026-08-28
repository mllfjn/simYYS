package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.citiao;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackInfo;

public class CiTiao6DouHun {
    public static final String CiTiaoName = "斗魂";

    public static void install(Character character) {
        Status.of(CiTiaoName, character)
                .runOn(Trigger.BEING_ATTACKED, triggerParam -> {
                    AttackInfo attackInfo = ((ParamAttackInfo) triggerParam).getAttackInfo();
                    if (!attackInfo.isCrit()) {
                        // 首领受到非暴击伤害降低80%
                        attackInfo.getTraceableNumber().mul(0.2, CiTiaoName);
                    }
                })
                .addTo();

        character.bp.addPriorityMove(character, () -> {
            Character maxCritPower = new CharacterFinder(character)
                    .filterEnemy()
                    .get(Attribute.CRIT_POWER, CharacterFinder.Criteria.MAX);
            Status.of(CiTiaoName + "攻击加成", character, maxCritPower)
            maxCritPower.addStatus(new StatusDHAttack(character, maxCritPower));
        });
    }

    static class StatusDHAttack extends Status implements AttributeModifier {

        public StatusDHAttack(Character from, Character belongTo) {
            super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.ATTACK;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            // 己方暴击伤害最高的单位获得40%攻击甲醇
            return belongTo.getInitBaseAttack() * 0.4;
        }
    }
}
