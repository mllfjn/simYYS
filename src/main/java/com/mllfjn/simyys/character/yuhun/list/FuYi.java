package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunSealResponse;

public class FuYi extends YuHun implements YuHunSealResponse {
    public static final String YuHunName = "蝠翼";
    private static final Skill skill = Skill.getInstance(YuHunName);

    private StatusFYListener status;

    @Override
    public void init(Character character, boolean isInit) {
        super.init(character, isInit);
        status = new StatusFYListener(character);
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

    static class StatusFYListener extends Status implements StatusRunnable {
        public StatusFYListener(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.CAUSE_ATTACK;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            if (param instanceof ParamAttackInfo pai) {
                belongTo.doInteractive(interactive -> {
                    interactive.recovery(skill, belongTo,
                            0.2 * pai.getAttackInfo().getTraceableNumber().getNumber()
                    );
                    skill.useDone();
                });
            }
            return false;
        }
    }
}
