package com.mllfjn.simyys.character.list.sp.shenshe;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.instance.StatusSleep;
import com.mllfjn.simyys.character.status.Trigger;

public class StatusDuoHua extends Status {
    public StatusDuoHua(Character from, Character belongTo) {
        super("堕化", from, belongTo);
        type(StatusType.GENERAL, StatusForm.YIN_JI);
        displayName();
        attribute(Attribute.SPEED, _ -> belongTo.getInitSpeed() * 0.4);
        // 回合开始时腐蚀自身当前24%生命,视为失去生命,且可解除睡眠状态
        runOn(Trigger.BEFORE_ROUND, _ -> {
            belongTo.lostHP(belongTo.getHp() * 0.24);
            StatusSleep.removeSleep(belongTo);
        });
        runOn(Trigger.DIE, _ -> {
            // 携带者阵亡时,神堕八岐大蛇提升40%行动条
            belongTo.doInteractive(interactive -> interactive.increaseLocation(from, 40));
            // 并将携带者献祭成1把堕落之剑
            new DuoLuoZhiJian((ShenShe) from, belongTo, false);
            // 立即破除1把天羽羽斩封印
            ((ShenShe) from).poChuZhenYa();
        });
    }

    public void disable() {
        removeAction(Trigger.DIE);
    }
}
