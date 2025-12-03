package com.mllfjn.simyys.character.yuhun;

import com.mllfjn.simyys.interactive.Info;

public class KuangGu extends YuHun implements YuHunEffectInfo {
    public static final String YuHunName = "狂骨";

    @Override
    public void effectInfo(Info info) {
        int guiHuoCount = character.bp.getGuiHuoCount(character);
        if (guiHuoCount == 0) {
            return;
        }
        // 造成伤害时，每拥有1点鬼火，提升8%伤害
        info.getTraceableNumber().mul(1 + 0.08 * guiHuoCount, YuHunName);
        yuHunEffect();
    }

    @Override
    public String getName() {
        return YuHunName;
    }
}
