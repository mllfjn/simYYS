package com.mllfjn.simyys.character.list.mob.jifengmo.citiao;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.AttributeModifier;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;
import com.mllfjn.simyys.character.status.determinant.InfluenceDamageBeingAttack;
import com.mllfjn.simyys.interactive.InteractiveInfo;
import com.mllfjn.simyys.interactive.AttackType;

public class CiTiao2YiSui {
    public static final String CiTiaoName = "易碎";

    public static void install(Character character) {
        character.addStatus(new StatusYiSui(character));
    }

    static class StatusYiSui extends Status implements AttributeModifier, InfluenceDamageBeingAttack {
        // 自身降低35%的防御
        public StatusYiSui(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.DEFENCE;
        }

        @Override
        public double getInfluence(Attribute attribute) {
            return -0.35 * belongTo.getInitDefense();
        }

        @Override
        public void doInfluenceBeingAttack(AttackType attackType, InteractiveInfo interactiveInfo) {
            // 首领受到的间接伤害降低99%
            if (attackType == AttackType.JIAN_JIE) {
                interactiveInfo.getTraceableNumber().mul(0.01, CiTiaoName);
            }
        }
    }
}
