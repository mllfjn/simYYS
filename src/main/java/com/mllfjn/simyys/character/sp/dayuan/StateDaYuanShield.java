package com.mllfjn.simyys.character.sp.dayuan;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.state.StateSettleType;
import com.mllfjn.simyys.state.StateShield;

public class StateDaYuanShield extends StateShield {
    private StateDaYuanShield(Character from, Character belongTo) {
        super(from, belongTo, from.getMaxHp() * 0.08);
        setSettleType(StateSettleType.CHI_XU, 2);
    }

    private void refresh() {
        setShield(from.getMaxHp() * 0.08);
        setDuration(2);
    }

    public static void install(Character daYuan, Character target) {
        if (target.getHp() == target.getMaxHp()) {
            target.getState(StateDaYuanShield.class)
                    .ifPresentOrElse(StateDaYuanShield::refresh
                            , () -> target.addState(new StateDaYuanShield(daYuan, target)));
        }
    }
}
