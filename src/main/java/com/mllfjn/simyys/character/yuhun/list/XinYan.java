package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunAttack;
import com.mllfjn.simyys.interactive.AttackInfo;

public class XinYan extends YuHun implements YuHunAttack {
    public static final String YuHunName = "心眼";

    @Override
    public String getName() {
        return YuHunName;
    }

    @Override
    public void effectInfo(AttackInfo attackInfo) {
        Character target = attackInfo.getTarget();
        int count = (int) ((target.getMaxHp() - target.getHp()) / target.getMaxHp());
        if (count > 0) {
            attackInfo.getTraceableNumber().mul(1 + 0.1 * count, YuHunName);
        }
    }
}
