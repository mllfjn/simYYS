package com.mllfjn.simyys.character.list.ssr.geye;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAddCrowdControl;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;

class StatusHZBH extends Status {
    private static final String StatusName = "狐族庇护";

    private int stack;

    private boolean continueEffective;

    private StatusHZBH(Character from, Character belongTo, int stack) {
        super(StatusName, from, belongTo, StatusType.BUFF, StatusForm.YIN_JI);
        this.stack = stack;

        duration(StatusDurationType.CHI_XU, 1);
        display(() -> StatusName + stack);

        runOn(Trigger.BEING_ATTACKED, param -> {
            ParamAttackInfo pai = (ParamAttackInfo) param;
            pai.getAttackInfo().setCancel(true);
            setContinueEffective(pai.getAttackInfo().getSkill());
        });

        runOn(Trigger.ADDING_CROWD_CONTROL, param -> {
            ParamAddCrowdControl pac = (ParamAddCrowdControl) param;
            pac.getEffectInfo().setCancel(true);
            setContinueEffective(pac.getEffectInfo().getSkill());
        });
    }

    static void addStack(Character from, Character belongTo, int stack, boolean isIncreaseSpeed) {
        belongTo.getStatus(StatusHZBH.class).ifPresentOrElse(
                status -> {
                    status.stack += stack;
                    if (status.stack > 3) {
                        status.stack = 3;
                    }
                    status.duration(1);
                },
                () -> belongTo.addStatus(new StatusHZBH(from, belongTo, stack))
        );

        // 二-lv3加速效果
        if (isIncreaseSpeed && from != belongTo) {
            StatusHZBHSpeed.addStack(from);
        }
    }

    private void setContinueEffective(Skill skill) {
        if (!continueEffective) {
            continueEffective = true;
            skill.addSkillEndListener(() -> {
                if (stack > 1) {
                    stack--;
                } else {
                    delete();
                }
                continueEffective = false;
            });
        }
    }

    static class StatusHZBHSpeed extends Status {
        private static final String StatusName = "加速";

        private int stack = 1;

        private StatusHZBHSpeed(Character character, int duration) {
            super(StatusHZBH.StatusName + StatusName, character, character, StatusType.BUFF, StatusForm.ZHUANG_TAI);
            duration(StatusDurationType.CHI_XU, duration);
            display(() -> StatusName + stack);
            attribute(Attribute.SPEED, _ -> 75.0 * stack);
        }

        static void addStack(Character character) {
            int shouldDuration = character.isInRound() ? 2 : 1;
            StatusHZBHSpeed statusHZBHSpeed = null;
            int stackNow = 0;
            for (Status status : character.getStatuses()) {
                if (status instanceof StatusHZBHSpeed s) {
                    stackNow += s.stack;
                    if (stackNow == 3) {
                        return;
                    }
                    if (status.getDuration() == shouldDuration) {
                        statusHZBHSpeed = s;
                    }
                }
            }

            if (statusHZBHSpeed != null) {
                statusHZBHSpeed.stack++;
            } else {
                character.addStatus(new StatusHZBHSpeed(character, shouldDuration));
            }
        }
    }
}
