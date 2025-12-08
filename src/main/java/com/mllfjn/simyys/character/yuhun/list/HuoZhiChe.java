package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.EventRoundDone;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.Runnable;
import com.mllfjn.simyys.character.status.instance.StatusForceChangeCost;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunSealResponse;

public class HuoZhiChe extends YuHun implements YuHunSealResponse {
    public static final String YuHunName = "火之车";

    private StatusAfterRound statusAfterRound;

    @Override
    public void init(Character character) {
        super.init(character);
        statusAfterRound = new StatusAfterRound(character);
    }

    @Override
    public String getName() {
        return YuHunName;
    }

    @Override
    public void enable() {
        character.addStatus(statusAfterRound);
    }

    @Override
    public void disable() {
        character.removeStatus(statusAfterRound);
    }

    static class StatusAfterRound extends Status implements Runnable, Displayable {
        private int stack;

        public StatusAfterRound(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public String getText() {
            if (stack == 0) {
                return null;
            }
            return "墓火" + stack;
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.AFTER_ROUND;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            // 携带者回合结束时,获得1层墓火
            stack++;
            // 累计4层时清空层数并获得1个回合,且该回合鬼火消耗减少1点
            if (stack == 4) {
                stack = 0;
                belongTo.getInteractive().getNewRound(belongTo);
                belongTo.bp.addActionListener(belongTo, event -> {
                    if (event instanceof EventRoundDone) {
                        belongTo.addStatus(new StatusForceChangeCost(belongTo, 1));
                        return true;
                    }
                    return false;
                });
            }
            return false;
        }
    }
}
