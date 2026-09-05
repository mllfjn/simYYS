package com.mllfjn.simyys.character.list.sp.dayuan;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.Trigger;
import com.mllfjn.simyys.character.status.triggerParam.ParamLocationChange;

// 该状态在结缘目标身上,belongTo是结缘目标,from是大缘
abstract class StatusShengTian extends Status {
    public StatusShengTian(DaYuan from, Character belongTo) {
        super("胜天之缘-拉条", from, belongTo);
        StatusCombined combined = new StatusCombined(belongTo, from, this);

        // 免疫推条
        runOn(Trigger.LOCATION_WILL_CHANGE, param -> {
            ParamLocationChange plc = (ParamLocationChange) param;
            if (plc.isFromDecrease) {
                plc.cancel();
            }
        });

        // 回合结束后拉条对方,禁止再次触发,等待对方行动后允许拉条自己
        runOn(Trigger.AFTER_ROUND, _ -> {
            // 这里拉条来源必须要是from-大缘自己,因为大缘免疫其他目标拉条
            from.doInteractive(interactive -> interactive.increaseLocation(from, 30));
            disableAction(Trigger.AFTER_ROUND);
            combined.enableAction(Trigger.AFTER_ACTION);
        });

        // 重新允许对方拉条自己,禁止再次触发允许
        runOnAndDisable(Trigger.AFTER_ACTION, _ -> {
            combined.enableAction(Trigger.AFTER_ROUND);
            disableAction(Trigger.AFTER_ACTION);
        });
    }
}


class StatusSTChi extends StatusShengTian {
    public StatusSTChi(DaYuan from, Character belongTo) {
        super(from, belongTo);
        display("缘·赤");
    }
}

class StatusSTQing extends StatusShengTian {
    public StatusSTQing(DaYuan from, Character belongTo) {
        super(from, belongTo);
        display("缘·青");
    }
}


