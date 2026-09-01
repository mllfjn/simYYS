package com.mllfjn.simyys.character.list.ssr.beimihu;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.Trigger;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;

public class StatusShiZhiXi extends Status {
    public static final String StatusName = "时之隙";

    private final double increase;

    public StatusShiZhiXi(Character from, Character belongTo, int level) {
        super(StatusName, from, belongTo);
        increase = level >= 4 ? 1.4 : level >= 2 ? 1.2 : 1;
        if (increase != 1) {
            runOn(Trigger.WHEN_ATTACK, param ->
                    ((ParamAttackInfo) param).getAttackInfo().getTraceableNumber().mul(increase, StatusName)
            );
        }
    }

    public static void install(Character from, Character belongTo, int level) {
        belongTo.addStatus(new StatusShiZhiXi(from, belongTo, level));
        belongTo.bp.situation.getSZXNewRound(belongTo);
    }
}
