package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.EventBattleStart;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.instance.StatusBiHu;
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
        character.bp.addActionListener(character, event -> {
            if (event instanceof EventBattleStart) {
                if (character.bp.isMobBattle(character)) {
                    if (character.getStatus(StatusBiHuSQL.class).isEmpty()) {
                        character.addStatus(new StatusBiHuSQL(character));
                    }
                }
                return true;
            }
            return false;
        });
    }


    static class StatusBiHuSQL extends StatusBiHu {

        public StatusBiHuSQL(Character character) {
            super(character, character);
        }

        @Override
        public void beforeDelete() {
            belongTo.addStatus(new StatusSQLListener(belongTo));
        }
    }

    static class StatusSQLListener extends Status {

        public StatusSQLListener(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
            setDurationType(StatusDurationType.CHI_XU, 5);
        }

        @Override
        public void beforeDelete() {
            belongTo.addStatus(new StatusBiHuSQL(belongTo));
        }
    }
}
