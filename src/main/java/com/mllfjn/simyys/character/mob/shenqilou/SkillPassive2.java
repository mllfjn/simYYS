package com.mllfjn.simyys.character.mob.shenqilou;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.Runnable;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;
import com.mllfjn.simyys.character.status.Trigger;
import com.mllfjn.simyys.character.status.triggerParam.ParamAfterAttack;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;

class SkillPassive2 extends Skill implements PassiveSkill {
    public static final String SkillName = "蜃气升腾";

    private final StatusBeAttack statusBeAttack;

    public SkillPassive2(Character belongTo) {
        super(belongTo, 0, 0, 0, 6);
        this.statusBeAttack = new StatusBeAttack(belongTo);
    }

    @Override
    public void enable() {
        getBelongTo().addStatus(statusBeAttack);
    }

    @Override
    public void disable() {
        getBelongTo().removeStatus(statusBeAttack);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public void usePrivate(BattlePane bp) {

    }

    static class StatusBeAttack extends Status implements Runnable {
        private int count = 0;

        public StatusBeAttack(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.AFTER_ATTACK;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            if (param instanceof ParamAfterAttack pa) {
                if (!pa.attackInfo.isCrit()) {
                    // 蜃气楼每受到3次非暴击伤害，获得[蜃雾笼罩]
                    count++;
                    if (count == 3) {
                        count = 0;
                        StatusShenWuHuDun.get(belongTo);
                    }
                }
            }
            return false;
        }
    }
}
