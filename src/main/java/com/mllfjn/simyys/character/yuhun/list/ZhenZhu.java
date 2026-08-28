package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.StatusDurationType;
import com.mllfjn.simyys.character.status.instance.StatusShield;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.interactive.TraceableNumber;

public class ZhenZhu extends YuHun {
    public static final String YuHunName = "珍珠";

    @Override
    public String getName() {
        return YuHunName;
    }

    public void doInteractive(Character target, TraceableNumber traceableNumber) {
        target.addStatus(new StatusZZShield(character, target,
                traceableNumber.getNumber() * 0.3));
    }

    static class StatusZZShield extends StatusShield {

        public StatusZZShield(Character from, Character belongTo, double shield) {
            super(from, belongTo, shield);
            setDurationType(StatusDurationType.CHI_XU, 2);
        }
    }
}
