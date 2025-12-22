package com.mllfjn.simyys.character.list.mob.jifengmo.citiao;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;
import com.mllfjn.simyys.character.status.determinant.InfluenceDamageBeingAttack;
import com.mllfjn.simyys.interactive.InteractiveInfo;
import com.mllfjn.simyys.interactive.AttackType;

public class CiTiao3ZhouShu {
    public static final String CiTiaoName = "咒术";

    public static void install(Character character) {
        character.addStatus(new StatusZhouShu(character));
    }

    static class StatusZhouShu extends Status implements InfluenceDamageBeingAttack {

        public StatusZhouShu(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public void doInfluenceBeingAttack(AttackType attackType, InteractiveInfo interactiveInfo) {
            // 传导伤害增加30%
            if (attackType == AttackType.CHUAN_DAO) {
                interactiveInfo.getTraceableNumber().mul(1.3, CiTiao3ZhouShu.CiTiaoName);
            }
        }
    }
}
