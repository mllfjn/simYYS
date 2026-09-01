package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
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

    class StatusLRD extends Status {
        private boolean canTrigger = true;

        public StatusLRD(Character character) {
            super(YuHunName, character);
            runOn(Trigger.AFTER_ROUND, _ -> {
                if (canTrigger) {
                    if (RateController.yuHun(belongTo, LunRuDao.this, RATE)) {
                        belongTo.doInteractive(interactive -> interactive.getNewRound(belongTo));
                        LunRuDao.this.yuHunEffect();
                        canTrigger = false;
                    }
                } else {
                    canTrigger = true;
                }
            });
        }
    }
}
