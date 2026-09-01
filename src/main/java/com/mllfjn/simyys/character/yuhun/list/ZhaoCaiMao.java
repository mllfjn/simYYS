package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunSealResponse;
import com.mllfjn.simyys.ratecontroller.RateController;

public class ZhaoCaiMao extends YuHun implements YuHunSealResponse {
    public static final String YuHunName = "招财猫";

    private Status status;

    @Override
    public void init(Character character, boolean isInit) {
        super.init(character, isInit);
        status = Status.of(YuHunName, character)
                .retainAfterChangeWave()
                .retainAfterDie()
                .runOn(Trigger.BEFORE_ROUND, _ -> {
                    if (RateController.yuHun(character, ZhaoCaiMao.this, 50)) {
                        ZhaoCaiMao.this.yuHunEffect();
                        character.bp().gainGuiHuo(character, 2);
                    }
                });
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
}
