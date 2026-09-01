package com.mllfjn.simyys.character.list.ssr.shijiamei;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;

class StatusXuWangMiZhang extends Status {
    private static final String StatusName = "虚妄迷障";

    private StatusXuWangMiZhang(Character from, Character belongTo) {
        super(StatusName, from, belongTo);
        duration(StatusDurationType.CHI_XU, 2);
        forceChangeSkillCost(3);
        displayNameAndDuration();
        runOn(Trigger.WHEN_ATTACK, param ->
                ((ParamAttackInfo) param).getAttackInfo().getTraceableNumber().mul(0.15, StatusName)
        );
    }

    static void install(Character from, Character belongTo) {
        belongTo.getStatus(StatusXuWangMiZhang.class)
                .ifPresentOrElse(
                        status -> status.duration(2),
                        () -> belongTo.addStatus(new StatusXuWangMiZhang(from, belongTo))
                );
    }
}
