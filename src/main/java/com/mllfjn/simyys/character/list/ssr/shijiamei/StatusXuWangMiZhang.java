package com.mllfjn.simyys.character.list.ssr.shijiamei;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.determinant.InfluenceDamageWhenAttack;
import com.mllfjn.simyys.interactive.AttackInfo;

class StatusXuWangMiZhang extends Status implements Displayable, InfluenceDamageWhenAttack, ForceChangeCost {
    private static final String StatusName = "虚妄迷障";

    private StatusXuWangMiZhang(Character from, Character belongTo) {
        super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
        setDurationType(StatusDurationType.CHI_XU, 2);
    }

    static void install(Character from, Character belongTo) {
        belongTo.getStatus(StatusXuWangMiZhang.class)
                .ifPresentOrElse(
                        status -> status.setDuration(2),
                        () -> belongTo.addStatus(new StatusXuWangMiZhang(from, belongTo))
                );
    }

    @Override
    public String getDisplayText() {
        return StatusName + getDuration();
    }

    @Override
    public void doInfluenceWhenAttack(AttackInfo attackInfo) {
        attackInfo.getTraceableNumber().mul(0.15, StatusName);
    }

    @Override
    public int getChange() {
        return +3;
    }
}
