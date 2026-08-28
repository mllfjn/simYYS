package com.mllfjn.simyys.character.list.sp.dayuan;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.StatusDurationType;
import com.mllfjn.simyys.character.status.instance.StatusShield;

public class StatusDaYuanShield extends StatusShield {
    private StatusDaYuanShield(Character from, Character belongTo) {
        super(from, belongTo, from.getMaxHp() * 0.08);
        setDurationType(StatusDurationType.CHI_XU, 2);
    }

    private void refresh(boolean isCrit) {
        double base = from.getMaxHp() * 0.08;
        if (isCrit) {
            base *= from.getCritPower() / 100;
        }
        setShield(base);
        setDuration(2);
    }

    public static void install(Character daYuan, Character target, boolean isCrit) {
        target.getStatus(StatusDaYuanShield.class)
                .ifPresentOrElse((status) -> status.refresh(isCrit)
                        , () -> target.addStatus(new StatusDaYuanShield(daYuan, target)));

    }
}
