package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.instance.StatusBiHu;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunUnfullMark;

public class ShenQiLou extends YuHun implements YuHunUnfullMark {
    public static final String YuHunName = "蜃气楼";

    @Override
    public String getName() {
        return YuHunName;
    }

    @Override
    public void init(Character character, boolean isInit) {
        super.init(character, isInit);

        // 与怪物的战斗开始时，获得庇护
        character.bp.addPriorityMove(character, () -> {
            if (character.bp.isMobBattle(character)) {
                character.addStatus(new StatusBiHuSQL(character));
            }
        });
    }


    static class StatusBiHuSQL extends StatusBiHu {
        private int cooling = 0;

        public StatusBiHuSQL(Character character) {
            super(character, character);
            runOnAndDisable(Trigger.AFTER_ROUND, _ -> {
                if (cooling == 1) {
                    enableAction(Trigger.ADDING_CROWD_CONTROL);
                    disableAction(Trigger.AFTER_ROUND);
                } else {
                    cooling--;
                }
            });
        }

        @Override
        protected void used() {
            disableAction(Trigger.ADDING_CROWD_CONTROL);
            cooling = 5;
        }
    }
}
