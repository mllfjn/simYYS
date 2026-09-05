package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.character.yuhun.Equip;
import com.mllfjn.simyys.character.yuhun.YuHunAttack;
import com.mllfjn.simyys.interactive.AttackInfo;

public class KuangGu extends Equip implements YuHunAttack {
    public static final String YuHunName = "狂骨";

    @Override
    public void effectInfo(AttackInfo attackInfo) {
        int guiHuoCount = character.bp.getGuiHuoCount(character);
        if (guiHuoCount == 0) {
            return;
        }
        // 造成伤害时，每拥有1点鬼火，提升8%伤害
        attackInfo.getTraceableNumber().mul(1 + 0.08 * guiHuoCount, YuHunName);
        yuHunEffect();
    }

    @Override
    public String getName() {
        return YuHunName;
    }
}
