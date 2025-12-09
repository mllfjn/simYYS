package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusDurationType;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;
import com.mllfjn.simyys.character.status.determinant.InfluenceDamageWhenAttack;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.AttackType;

public class HaiYueHuoYu extends YuHun {
    public static final String YuHunName = "海月火玉";

    public void enable() {
        character.addStatus(new StatusHaiYue(character));
    }

    @Override
    public String getName() {
        return YuHunName;
    }

    static class StatusHaiYue extends Status implements InfluenceDamageWhenAttack {

        public StatusHaiYue(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
            setDurationType(StatusDurationType.CHI_XU, 1);
        }

        @Override
        public void doInfluenceWhenAttack(AttackType attackType, AttackInfo attackInfo) {
            attackInfo.getTraceableNumber().mul(1.4, YuHunName);
        }
    }
}
