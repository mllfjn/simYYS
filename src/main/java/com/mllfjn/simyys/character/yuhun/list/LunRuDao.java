package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunSealResponse;
import com.mllfjn.simyys.ratecontroller.RateController;

public class LunRuDao extends YuHun implements YuHunSealResponse {
    public static final String YuHunName = "轮入道";
    private static final int RATE = 20;

    private StatusLRD status;

    @Override
    public String getName() {
        return YuHunName;
    }

    @Override
    public void init(Character character, boolean isInit) {
        super.init(character, isInit);
        status = new StatusLRD(character);
    }

    @Override
    public void enable() {
        character.addStatus(status);
    }

    @Override
    public void disable() {
        character.removeStatus(status);
    }

    class StatusLRD extends Status implements StatusRunnable {

        public StatusLRD(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.AFTER_ROUND && !belongTo.isHaveStatus(StatusLRDMark.class);
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            if (RateController.yuHun(belongTo, LunRuDao.this, belongTo.bp.calc, RATE)) {
                belongTo.doInteractive(interactive -> interactive.getNewRound(belongTo));
                belongTo.addStatus(new StatusLRDMark(belongTo));
                LunRuDao.this.yuHunEffect();
            }
            return false;
        }

        static class StatusLRDMark extends Status {
            public StatusLRDMark(Character character) {
                super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
                setDurationType(StatusDurationType.CHI_XU, 2);
            }
        }
    }
}
