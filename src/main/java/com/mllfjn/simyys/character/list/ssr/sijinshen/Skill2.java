package com.mllfjn.simyys.character.list.ssr.sijinshen;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.BattleActionListener;
import com.mllfjn.simyys.battleevent.BattleEvent;
import com.mllfjn.simyys.battleevent.EventActionDone;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;

class Skill2 extends PassiveSkill {
    private static final String SkillName = "警醒";
    private static final int[] coefficients = new int[]{0, 12, 12, 16, 16, 20};

    private boolean teammateUsedSkill = false;

    public Skill2(SiJinShen belongTo, int level) {
        super(belongTo, level, 2);
        if (level >= 2) {
            belongTo.bp.addStatusAdder(c ->
                    c.team == belongTo.team
                            ? new StatusUseSkillListener(belongTo, c)
                            : null
            );
            belongTo.bp.addActionListener(new BattleActionListener(belongTo) {
                @Override
                public boolean onBattleAction(BattleEvent event) {
                    if (event instanceof EventActionDone) {
                        if (teammateUsedSkill) {
                            teammateUsedSkill = false;
                        } else {
                            belongTo.addXinYangStack();
                        }
                    }
                    return false;
                }
            });
            if (level >= 4) {
                belongTo.getNewRoundWhenXinYangRemoved = true;
            }
        }
    }

    double getCoefficient() {
        return 0.01 * coefficients[getLevel()];
    }

    @Override
    public String getName() {
        return SkillName;
    }

    private class StatusUseSkillListener extends Status implements StatusRunnable {
        public StatusUseSkillListener(Character from, Character belongTo) {
            super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.WILL_USE_SKILL;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            Skill2.this.teammateUsedSkill = true;
            return false;
        }
    }
}
