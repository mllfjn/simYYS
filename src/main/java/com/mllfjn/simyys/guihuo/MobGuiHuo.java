package com.mllfjn.simyys.guihuo;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.state.*;
import com.mllfjn.simyys.state.Runnable;
import com.mllfjn.simyys.trigger.Trigger;

public class MobGuiHuo extends State implements Runnable, Displayable {
    public static final String privateName = "怪物鬼火";
    int now;
    int max = 3;

    public MobGuiHuo(Character belongTo) {
        super(belongTo, belongTo, StateType.SPECIAL, StateForm.SPECIAL);
    }

    @Override
    public void setName() {
        name = privateName;
    }

    public boolean canUse(int num) {
        return now >= num;
    }

    public void useGuiHuo(int num) {
        now -= num;
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return trigger == Trigger.BEFORE_ROUND;
    }

    @Override
    public void run(Trigger trigger, BattlePane bp) {
        now = Math.min(now + 1, max);
    }

    @Override
    public String getText() {
        return "鬼火" + now;
    }
}
