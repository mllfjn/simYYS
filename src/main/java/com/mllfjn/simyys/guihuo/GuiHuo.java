package com.mllfjn.simyys.guihuo;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.state.Runnable;
import com.mllfjn.simyys.trigger.Trigger;

public class GuiHuo {
    private int max = 8;
    private int now;
    private int increment = 3;
    private int progress;
    public GuiHuo(int startWith) {
        now = startWith;
    }

    public boolean canUseGuiHuo(int num) {
        return now >= num;
    }

    public void useGuiHuo(int num) {
        now -= num;
    }

    public void addProgress() {
        progress++;
        if (progress == 5) {
            progress = 0;
            addGuiHuo(increment);
            if (increment < 5) {
                increment++;
            }
        }
    }

    public void addGuiHuo(int num) {
        now += num;
        now = Math.min(now, max);
    }

    public void setMax(int num) {
        max = num;
        now = Math.min(now, max);
    }

    public static boolean mobCanUseGuiHuo(Character character, int num) {
        MobGuiHuo guiHuo = (MobGuiHuo) character.getState(MobGuiHuo.privateName);
        return guiHuo.canUse(num);
    }
}
