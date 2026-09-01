package com.mllfjn.simyys.character.list.sp.dayuan;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;

// 该状态在大缘身上,belongTo是大缘,from是结缘目标
class StatusCombined extends Status {
    public StatusCombined(Character from, DaYuan belongTo, StatusShengTian shengTian) {
        super("胜天之缘-拉条", from, belongTo);
        // 自身(与处于)胜天之缘的目标回合结束后,均会提升对方30%行动条

        // 回合结束后拉条对方,禁止再次触发,等待对方行动后允许拉条自己
        runOn(Trigger.AFTER_ROUND, _ -> {
            belongTo.doInteractive(interactive -> interactive.increaseLocation(from, 30));
            disableAction(Trigger.AFTER_ROUND);
            shengTian.enableAction(Trigger.AFTER_ACTION);
        });
        // 重新允许对方拉条自己,禁止再次触发允许
        runOnAndDisable(Trigger.AFTER_ACTION, _ -> {
            shengTian.enableAction(Trigger.AFTER_ROUND);
            disableAction(Trigger.AFTER_ACTION);
        });

        addTo();
    }
}
