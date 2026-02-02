package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusType;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunAfterCauseAttack;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.Interactive;
import com.mllfjn.simyys.ratecontroller.RateController;

public class RiNvSiShi extends YuHun implements YuHunAfterCauseAttack {
    public static final String YuHunName = "日女巳时";


    @Override
    public void init(Character character, boolean isInit) {
        super.init(character, isInit);
    }

    @Override
    public String getName() {
        return YuHunName;
    }

    @Override
    public void action(AttackInfo attackInfo, Interactive interactive) {
        Character target = attackInfo.getTarget();
        if (RateController.yuHun(character, this, getRate(target))) {
            interactive.decreaseLocation(target, 30);
            yuHunEffect();
        }
    }

    private double getRate(Character target) {
        for (Status status : target.getStatuses()) {
            if (status.statusType == StatusType.BUFF) {
                return 30;
            }
        }
        return 20;
    }
}
