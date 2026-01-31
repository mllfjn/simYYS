package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAfterAttack;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunSealResponse;
import com.mllfjn.simyys.character.yuhun.YuHunUnfullMark;
import com.mllfjn.simyys.ratecontroller.RateController;

public class LongChe extends YuHun implements YuHunUnfullMark, YuHunSealResponse {
    public static final String YuHunName = "胧车";
    private StatusLCListener status;

    @Override
    public String getName() {
        return YuHunName;
    }

    @Override
    public void init(Character character, boolean isInit) {
        super.init(character, isInit);
        status = new StatusLCListener(character);
    }

    @Override
    public void enable() {
        character.addStatus(status);
    }

    @Override
    public void disable() {
        character.removeStatus(status);
    }

    class StatusLCListener extends Status implements StatusRunnable {
        private Skill causeSkill;

        public StatusLCListener(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.AFTER_ATTACK && causeSkill == null;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            if (param instanceof ParamAfterAttack paa && paa.interactiveInfo.getAttacker().isMob()) {
                if (RateController.yuHun(belongTo, LongChe.this, 50)) {
                    causeSkill = paa.interactiveInfo.getSkill();
                    LongChe.this.yuHunEffect();
                    belongTo.doInteractive(interactive -> interactive.increaseLocation(belongTo, 30));
                    causeSkill.addSkillEndListener(() -> causeSkill = null);
                }
            }
            return false;
        }
    }
}
