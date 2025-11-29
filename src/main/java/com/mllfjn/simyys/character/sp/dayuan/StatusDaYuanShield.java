package com.mllfjn.simyys.character.sp.dayuan;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.status.StatusDurationType;
import com.mllfjn.simyys.status.StatusShield;

public class StatusDaYuanShield extends StatusShield {
    private StatusDaYuanShield(Character from, Character belongTo) {
        super(from, belongTo, from.getMaxHp() * 0.08);
        setSettleType(StatusDurationType.CHI_XU, 2);
    }

    private void refresh() {
        setShield(from.getMaxHp() * 0.08);
        setDuration(2);
    }

    public static void install(Character daYuan, Character target) {
        if (target.getHp() == target.getMaxHp()) {
            target.getStatus(StatusDaYuanShield.class)
                    .ifPresentOrElse(StatusDaYuanShield::refresh
                            , () -> target.addStatus(new StatusDaYuanShield(daYuan, target)));
        }
    }
}
