package com.mllfjn.simyys.guihuo;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.Trigger;

public class MobGuiHuo extends Status {
    private int max;
    private int now;

    public MobGuiHuo(Character belongTo, int initGuiHuo, int max) {
        super("鬼火", belongTo);
        this.now = initGuiHuo;
        this.max = max;

        runOn(Trigger.BEFORE_ROUND, _ -> gainGuiHuo(1));
    }

    public static boolean mobCanUseGuiHuo(Character character, int num) {
        return character.getStatus(MobGuiHuo.class).orElseThrow().canUse(num);
    }
    public static void mobUseGuiHuo(Character character, int num) {
        character.getStatus(MobGuiHuo.class).orElseThrow().useGuiHuo(num);
    }
    public static void mobGainGuiHuo(Character character, int num) {
        character.getStatus(MobGuiHuo.class).orElseThrow().gainGuiHuo(num);
    }

    public void setGuiHuo(int num) {
        now = num;
    }

    public void setMax(int num) {
        max = num;
    }

    private boolean canUse(int num) {
        return now >= num;
    }

    private void useGuiHuo(int num) {
        if (now == max) {
            enableAction(Trigger.BEFORE_ROUND);
        }
        now -= num;
        changeDisplay();
    }

    private void gainGuiHuo(int num) {
        now = Math.min(now + num, max);
        if (now == max) {
            disableAction(Trigger.BEFORE_ROUND);
        }
        changeDisplay();
    }

    private void changeDisplay() {
        display(name + now);
    }
}
