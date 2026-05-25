package com.mllfjn.simyys.character.list.ssr.geye;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAddCrowdControl;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;

class StatusHZBH extends Status implements StatusRunnable, Displayable {
    private static final String StatusName = "狐族庇护";

    private int stack;

    private StatusHZBH(Character from, Character belongTo, int stack) {
        super(from, belongTo, StatusType.BUFF, StatusForm.YIN_JI);
        this.stack = stack;

        setDurationType(StatusDurationType.CHI_XU, belongTo.isInRound() ? 2 : 1);
    }

    static void addStack(Character from, Character belongTo, int stack, boolean isIncreaseSpeed) {
        belongTo.getStatus(StatusHZBH.class).ifPresentOrElse(
                status -> {
                    status.stack += stack;
                    if (status.stack > 3) {
                        status.stack = 3;
                    }
                    if (belongTo.isInRound()) {
                        status.setDuration(2);
                    }
                },
                () -> belongTo.addStatus(new StatusHZBH(from, belongTo, stack))
        );

        // 二-lv3加速效果
        if (isIncreaseSpeed && from != belongTo) {
            StatusHZBHSpeed.addStack(from);
        }
    }

    @Override
    public String getDisplayText() {
        return StatusName + stack;
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return trigger == Trigger.BEING_ATTACKED || trigger == Trigger.ADDING_CROWD_CONTROL;
    }

    @Override
    public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
        if (param instanceof ParamAttackInfo pai) {
            pai.getAttackInfo().setCancel(true);
        } else {
            ((ParamAddCrowdControl) param).getEffectInfo().setCancel(true);
        }

        if (stack > 1) {
            stack--;
            return false;
        } else {
            return true;
        }
    }

    static class StatusHZBHSpeed extends Status implements Displayable, AttributeModifier {
        private static final String StatusName = "加速";

        private int stack = 1;

        private StatusHZBHSpeed(Character character) {
            super(character, character, StatusType.BUFF, StatusForm.ZHUANG_TAI);
            int duration = character.isInRound() ? 2 : 1;
            setDurationType(StatusDurationType.CHI_XU, duration);
        }

        static void addStack(Character character) {
            character.getStatus(StatusHZBHSpeed.class).ifPresentOrElse(
                    statusHZBHSpeed -> {
                        if (statusHZBHSpeed.stack < 3) {
                            statusHZBHSpeed.stack++;
                        }
                    },
                    () -> character.addStatus(new StatusHZBHSpeed(character))
            );
        }

        @Override
        public String getDisplayText() {
            return StatusName + stack;
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.SPEED;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            return 75 * stack;
        }
    }
}
