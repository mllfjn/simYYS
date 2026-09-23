package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.TeamPane;
import com.mllfjn.simyys.character.yuhun.Equip;

public class HuoLing extends Equip {
    public static final String YuHunName = "火灵";

    public void action(TeamPane teamPane) {
        teamPane.gainGuiHuoFromYuHun(3);
        yuHunEffect();
    }

    @Override
    public String getName() {
        return YuHunName;
    }
}
