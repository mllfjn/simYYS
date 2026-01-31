package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunSealResponse;
import com.mllfjn.simyys.ratecontroller.RateController;

public class ZhaoCaiMao extends YuHun implements YuHunSealResponse {
    public static final String YuHunName = "招财猫";

    private StatusZCMListener status;

    @Override
    public void init(Character character, boolean isInit) {
        super.init(character, isInit);
        status = new StatusZCMListener(character);
    }

    @Override
    public String getName() {
        return YuHunName;
    }

    @Override
    public void enable() {
        character.addStatus(status);
    }

    @Override
    public void disable() {
        character.removeStatus(status);
    }

    class StatusZCMListener extends Status implements StatusRunnable {

        public StatusZCMListener(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.BEFORE_ROUND;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            if (RateController.yuHun(belongTo, ZhaoCaiMao.this, 50)) {
                ZhaoCaiMao.this.yuHunEffect();
                belongTo.bp.gainGuiHuo(belongTo, 2);
            }
            return false;
        }
    }
}
