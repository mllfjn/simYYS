package com.mllfjn.simyys.character.yuhun.list.youchizi;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.yuhun.YuHun;


public class YouChiZi extends YuHun {
    public static final String YuHunName = "油赤子";

    @Override
    public void init(Character character, boolean isInit) {
        super.init(character, isInit);
        character.bp.atBattleStart(() -> character.addStatus(new StatusYCZ(character)));
    }

    @Override
    public String getName() {
        return YuHunName;
    }
}
