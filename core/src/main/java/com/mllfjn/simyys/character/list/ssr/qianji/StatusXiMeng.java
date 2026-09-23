package com.mllfjn.simyys.character.list.ssr.qianji;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.Trigger;

class StatusXiMeng extends Status {
    public StatusXiMeng(Character from, Character belongTo) {
        super("汐梦", from, belongTo, StatusType.DEBUFF, StatusForm.YIN_JI);
        displayName();
        // 行动后消耗
        runOn(Trigger.AFTER_ROUND, _ -> {
            // TODO 扣除3点鬼火,对怪物不生效
            delete();
        });
    }
}
