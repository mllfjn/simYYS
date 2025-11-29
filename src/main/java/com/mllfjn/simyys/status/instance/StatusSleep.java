package com.mllfjn.simyys.status.instance;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.status.*;

// TODO 无法动作和受到伤害时自动移除效果没有写
public class StatusSleep extends Status implements Displayable {

    public StatusSleep(Character from, Character belongTo) {
        super(from, belongTo, StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
    }

    public static void removeSleep(Character character) {
        character.removeStatus(StatusSleep.class);
    }

    @Override
    public String getText() {
        return "沉睡" + getDuration();
    }
}
