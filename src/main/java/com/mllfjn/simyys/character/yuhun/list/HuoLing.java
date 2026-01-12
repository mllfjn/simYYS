package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.TeamPane;
import com.mllfjn.simyys.character.yuhun.YuHun;

public class HuoLing extends YuHun {
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
