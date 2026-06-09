package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunAttack;
import com.mllfjn.simyys.interactive.AttackInfo;

public class PianYeZhiWei extends YuHun implements YuHunAttack {
    public static final String YuHunName = "片叶之苇";

    @Override
    public String getName() {
        return YuHunName;
    }

    @Override
    public void effectInfo(AttackInfo attackInfo) {
        if (character.getHp() == character.getMaxHp()) {
            attackInfo.getTraceableNumber().mul(1.45, YuHunName);
            yuHunEffect();
        }
    }
}
