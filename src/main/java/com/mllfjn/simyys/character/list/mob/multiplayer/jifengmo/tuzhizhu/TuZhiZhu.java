package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.tuzhizhu;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.list.mob.multiplayer.MultiStageManager;
import com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.CharacterJiFengMoBase;
import com.mllfjn.simyys.character.propertygetter.PropertiesHolder;

public class TuZhiZhu extends CharacterJiFengMoBase {
    public static final String CharacterName = "土蜘蛛";

    @Override
    public void init(PropertiesHolder propertiesHolder, BattlePane bp) {
        super.init(propertiesHolder, bp);

        // 召唤右方大腿,中间背部,左方钳子
    }

    @Override
    protected void addStage(MultiStageManager multiStageManager) {

    }

    @Override
    protected void addOwnSkills() {

    }

    @Override
    protected String getJiFengMoSpeed() {
        return "200";
    }
}
