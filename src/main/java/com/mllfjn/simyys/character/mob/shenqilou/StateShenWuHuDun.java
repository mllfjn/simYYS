package com.mllfjn.simyys.character.mob.shenqilou;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.interactive.Info;
import com.mllfjn.simyys.state.StateShield;

public class StateShenWuHuDun extends StateShield {
    public static final String StateName = "蜃气护盾";

    private int count = 3;

    public StateShenWuHuDun(Character from, Character belongTo) {
        // 获得最大生命值100%蜃雾护盾
        super(from, belongTo, belongTo.getMaxHp());
    }

    @Override
    public boolean handle(Info info) {
        if (super.handle(info)) {
            return true;
        }
        // 使用暴击伤害攻击蜃气楼可击破护盾,抵挡3次暴击伤害后，护盾消失。
        if (info.isCrit()) {
            count--;
        }
        return count == 0;
    }

    @Override
    public String getText() {
        return StateName;
    }
}
