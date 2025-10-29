package com.mllfjn.simyys.guihuo;
import com.mllfjn.simyys.character.Character;

import java.io.Serializable;

public class GuiHuo implements Serializable {
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
            gainGuiHuo(increment);
            if (increment < 5) {
                increment++;
            }
        }
    }

    public void gainGuiHuo(int num) {
        now = Math.min(now + num, max);
    }

    public void setMax(int num) {
        max = num;
        now = Math.min(now, max);
    }

    public static boolean mobCanUseGuiHuo(Character character, int num) {
        MobGuiHuo guiHuo = (MobGuiHuo) character.getState(MobGuiHuo.privateName);
        return guiHuo.canUse(num);
    }

    public static void mobUseGuiHuo(Character character, int num) {
        MobGuiHuo guiHuo = (MobGuiHuo) character.getState(MobGuiHuo.privateName);
        guiHuo.useGuiHuo(num);
    }
}
