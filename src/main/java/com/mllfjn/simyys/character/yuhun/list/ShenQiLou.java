package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.Runnable;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.character.yuhun.YuHun;

public class ShenQiLou extends YuHun {
    public static final String YuHunName = "蜃气楼";

    @Override
    public String getName() {
        return YuHunName;
    }

    @Override
    public void init(Character character) {
        super.init(character);

        // 与怪物的战斗开始时，获得庇护
        if (character.bp.isMobBattle(character)) {

        }
    }


    static class StatusSQLListener extends Status implements Runnable {

        public StatusSQLListener(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return false;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            return false;
        }
    }
}
