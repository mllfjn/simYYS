package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunAttack;
import com.mllfjn.simyys.interactive.InteractiveInfo;

public class PoShi extends YuHun implements YuHunAttack {
    public static final String YuHunName = "破势";

    @Override
    public String getName() {
        return YuHunName;
    }

    @Override
    public void effectInfo(InteractiveInfo interactiveInfo) {
        if (interactiveInfo.getTarget().getHpPercent() > 0.7) {
            interactiveInfo.getTraceableNumber().mul(1.4, YuHunName);
            yuHunEffect();
        }
    }
}
