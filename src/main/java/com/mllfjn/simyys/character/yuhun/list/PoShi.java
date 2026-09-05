package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.character.yuhun.Equip;
import com.mllfjn.simyys.character.yuhun.YuHunAttack;
import com.mllfjn.simyys.interactive.AttackInfo;

public class PoShi extends Equip implements YuHunAttack {
    public static final String YuHunName = "破势";

    @Override
    public String getName() {
        return YuHunName;
    }

    @Override
    public void effectInfo(AttackInfo attackInfo) {
        if (attackInfo.getTarget().getHpPercent() > 0.7) {
            attackInfo.getTraceableNumber().mul(1.4, YuHunName);
            yuHunEffect();
        }
    }
}
