package com.mllfjn.simyys.state.instance;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.state.*;

// TODO 无法动作和受到伤害时自动移除效果没有写
public class StateSleep extends State implements Displayable {
    public static final String privateName = "沉睡";

    public StateSleep(Character from, Character belongTo, int duration) {
        super(from, belongTo, StateType.DEBUFF, StateForm.ZHUANG_TAI);
        setSettleType(StateSettleType.CHI_XU, duration);
    }

    public static void removeSleep(Character character) {
        character.removeState(privateName);
    }

    @Override
    public void setName() {
        name = privateName + getDuration();
    }

    @Override
    public String getText() {
        return privateName;
    }
}
