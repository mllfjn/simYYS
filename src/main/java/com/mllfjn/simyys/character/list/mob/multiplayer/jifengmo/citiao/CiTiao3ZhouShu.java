package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.citiao;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.AttackType;

public class CiTiao3ZhouShu {
    public static final String CiTiaoName = "咒术";

    public static void install(Character character) {
        Status.of(CiTiaoName + "-传导伤害增加", character)
                .runOn(Trigger.BEING_ATTACKED, triggerParam -> {
                    AttackInfo attackInfo = ((ParamAttackInfo) triggerParam).getAttackInfo();
                    // 传导伤害增加30%
                    if (attackInfo.getAttackType() == AttackType.CHUAN_DAO) {
                        attackInfo.getTraceableNumber().mul(1.3, CiTiao3ZhouShu.CiTiaoName);
                    }
                }).addTo();
    }
}
