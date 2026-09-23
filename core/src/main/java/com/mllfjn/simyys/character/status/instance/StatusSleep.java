package com.mllfjn.simyys.character.status.instance;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;

// TODO 无法动作和受到伤害时自动移除效果没有写
public class StatusSleep extends Status implements CrowdControl {

    public StatusSleep(Character from, Character belongTo) {
        super("沉睡", from, belongTo);
        type(StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
        displayNameAndDuration();
        runOn(Trigger.AFTER_ATTACK, _ -> delete());
    }

    public static void removeSleep(Character character) {
        character.removeStatus(StatusSleep.class);
    }
}
