package com.mllfjn.simyys.character.list.mob.jifengmo.citiao;

import com.mllfjn.simyys.battleevent.EventBattleStart;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.status.AttributeModifier;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;
import com.mllfjn.simyys.character.status.determinant.InfluenceDamageBeingAttack;
import com.mllfjn.simyys.interactive.InteractiveInfo;
import com.mllfjn.simyys.interactive.AttackType;

public class CiTiao6DouHun {
    public static final String CiTiaoName = "斗魂";

    public static void install(Character character) {
        character.addStatus(new StatusDHJianShang(character));
        character.bp.addActionListener(character, event -> {
            if (event instanceof EventBattleStart) {
                Character maxCritPower = new CharacterFinder(character)
                        .setTargetTeam(CharacterFinder.TargetTeam.ENEMY)
                        .get(Attribute.CRIT_POWER, CharacterFinder.Criteria.MAX);
                maxCritPower.addStatus(new StatusDHAttack(character, maxCritPower));
                return true;
            }
            return false;
        });
    }

    static class StatusDHJianShang extends Status implements InfluenceDamageBeingAttack {

        public StatusDHJianShang(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public void doInfluenceBeingAttack(AttackType attackType, InteractiveInfo interactiveInfo) {
            if (!interactiveInfo.isCrit()) {
                // 首领受到非暴击伤害降低80%
                interactiveInfo.getTraceableNumber().mul(0.2, CiTiaoName);
            }
        }
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
        public double getInfluence(Attribute attribute) {
            // 己方暴击伤害最高的单位获得40%攻击甲醇
            return belongTo.getInitBaseAttack() * 0.4;
        }
    }
}
