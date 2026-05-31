package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunSealResponse;

public class NiePanZhiHuo extends YuHun implements YuHunSealResponse {
    public static final String YuHunName = "涅槃之火";
    private static final Skill skill = Skill.getInstance("涅槃之火");

    private StatusNPListener status;

    @Override
    public void init(Character character, boolean isInit) {
        super.init(character, isInit);
        status = new StatusNPListener(character);
    }

    @Override
    public String getName() {
        return YuHunName;
    }

    @Override
    public void enable() {
        getBelongTo().addStatus(status);
    }

    @Override
    public void disable() {
        getBelongTo().removeStatus(status);
    }

    class StatusNPListener extends Status implements StatusRunnable {
        public StatusNPListener(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.AFTER_ACTION && belongTo.getHpPercent() < 0.3;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            belongTo.doInteractive(interactive -> {
                interactive.healTypical(skill, belongTo, 15);
                yuHunEffect();
                skill.useDone();
            });
            return false;
        }
    }
}
