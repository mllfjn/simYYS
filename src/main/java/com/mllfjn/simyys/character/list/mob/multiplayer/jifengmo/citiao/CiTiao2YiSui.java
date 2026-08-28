package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.citiao;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.AttackType;

public class CiTiao2YiSui {
    public static final String CiTiaoName = "易碎";

    public static void install(Character character) {
        new Status(CiTiaoName + "-减防", character)
                // 自身降低35%的防御
                .attribute(Attribute.DEFENCE, -0.35 * belongTo.getInitDefense())
                // 首领受到的间接伤害降低99%
                .runOn(Trigger.BEING_ATTACKED, triggerParam -> {
                    AttackInfo attackInfo = ((ParamAttackInfo) triggerParam).getAttackInfo();
                    if (attackInfo.getAttackType() == AttackType.JIAN_JIE) {
                        attackInfo.getTraceableNumber().mul(0.01, CiTiaoName);
                    }
                })
                .addTo();
    }
}
