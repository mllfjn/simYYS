package com.mllfjn.simyys.character.list.mob.jifengmo.shenqilou;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.Runnable;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;
import com.mllfjn.simyys.character.status.Trigger;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;

import java.util.Optional;

class SkillPassive1 extends Skill implements PassiveSkill {
    public static final String SkillName = "蜃雾笼罩";

    private final StatusAfterRound statusAfterRound;

    public SkillPassive1(Character belongTo) {
        super(belongTo, 0, 0, 0, 5);
        statusAfterRound = new StatusAfterRound(belongTo);
    }

    @Override
    public void enable() {
        getBelongTo().addStatus(statusAfterRound);
    }

    @Override
    public void disable() {
        getBelongTo().removeStatus(statusAfterRound);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        return Optional.empty();
    }

    static class StatusAfterRound extends Status implements Runnable {

        public StatusAfterRound(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.AFTER_ROUND;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            StatusShenWuHuDun.get(belongTo);
            return false;
        }
    }
}
