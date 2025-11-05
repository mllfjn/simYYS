package com.mllfjn.simyys.guihuo;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.state.*;
import com.mllfjn.simyys.state.Runnable;
import com.mllfjn.simyys.trigger.Trigger;

public class MobGuiHuo extends State implements Runnable, Displayable {
    private static final int max = 3;
    private int now;

    public MobGuiHuo(Character belongTo) {
        super(belongTo, belongTo, StateType.SPECIAL, StateForm.SPECIAL);
    }

    public static boolean mobCanUseGuiHuo(Character character, int num) {
        return character.getState(MobGuiHuo.class).orElseThrow().canUse(num);
    }
    public static void mobUseGuiHuo(Character character, int num) {
        character.getState(MobGuiHuo.class).orElseThrow().useGuiHuo(num);
    }
    public static void mobGainGuiHuo(Character character, int num) {
        character.getState(MobGuiHuo.class).orElseThrow().gainGuiHuo(num);
    }

    private boolean canUse(int num) {
        return now >= num;
    }
    private void useGuiHuo(int num) {
        now -= num;
    }
    private void gainGuiHuo(int num) {
        now = Math.min(now + num, max);
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return trigger == Trigger.BEFORE_ROUND && now < max;
    }
    @Override
    public boolean run(Trigger trigger, BattlePane bp) {
        now++;
        return false;
    }

    @Override
    public String getText() {
        return "鬼火" + now;
    }
}
