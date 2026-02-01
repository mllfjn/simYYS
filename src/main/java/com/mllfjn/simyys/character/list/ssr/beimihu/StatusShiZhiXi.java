package com.mllfjn.simyys.character.list.ssr.beimihu;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;
import com.mllfjn.simyys.character.status.determinant.InfluenceDamageWhenAttack;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.InteractiveInfo;

public class StatusShiZhiXi extends Status implements InfluenceDamageWhenAttack {
    public static final String StatusName = "时之隙";

    private final double increase;

    public StatusShiZhiXi(Character from, Character belongTo, int level) {
        super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
        increase = level >= 4 ? 1.4 : level >= 2 ? 1.2 : 1;
    }

    public static void enter(Character from, Character belongTo, int level) {
        belongTo.addStatus(new StatusShiZhiXi(from, belongTo, level));
        belongTo.bp.situation.getSZXNewRound(belongTo);
    }

    @Override
    public void doInfluenceWhenAttack(AttackInfo attackInfo) {
        if (increase != 1) {
            attackInfo.getTraceableNumber().mul(increase, StatusName);
        }
    }
}
