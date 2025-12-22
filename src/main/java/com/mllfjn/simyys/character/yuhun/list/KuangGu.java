package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunAttack;
import com.mllfjn.simyys.interactive.InteractiveInfo;

public class KuangGu extends YuHun implements YuHunAttack {
    public static final String YuHunName = "狂骨";

    @Override
    public void effectInfo(InteractiveInfo interactiveInfo) {
        int guiHuoCount = character.bp.getGuiHuoCount(character);
        if (guiHuoCount == 0) {
            return;
        }
        // 造成伤害时，每拥有1点鬼火，提升8%伤害
        interactiveInfo.getTraceableNumber().mul(1 + 0.08 * guiHuoCount, YuHunName);
        yuHunEffect();
    }

    @Override
    public String getName() {
        return YuHunName;
    }
}
