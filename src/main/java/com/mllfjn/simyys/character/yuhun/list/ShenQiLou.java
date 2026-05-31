package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.instance.StatusBiHu;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
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
        private boolean isEffective = true;
        private int cooling = 0;

        public StatusBiHuSQL(Character character) {
            super(character, character);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            if (isEffective) {
                return super.runnable(trigger);
            } else {
                return trigger == Trigger.AFTER_ROUND;
            }
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            if (isEffective) {
                return super.run(trigger, bp, param);
            } else {
                if (cooling == 1) {
                    isEffective = true;
                } else {
                    cooling--;
                }
                return false;
            }
        }

        @Override
        protected void used() {
            isEffective = false;
            cooling = 5;
        }
    }
}
