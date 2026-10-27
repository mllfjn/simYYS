package com.mllfjn.simyys.character.list.sp.huishihuaniaojuan;

import com.mllfjn.simyys.battleevent.StatusAdder;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;
import com.mllfjn.simyys.character.status.Trigger;
import com.mllfjn.simyys.character.status.triggerParam.ParamHealInfo;

class Skill2 extends PassiveSkill {
    private StatusAdder<StatusMoYing> adder;

    public Skill2(Character belongTo, int level) {
        super(belongTo, "墨气常萦", level, 2);
        if (level >= 4) {
            belongTo.bp().addPriorityMove(belongTo, () -> {
                if (isActive()) {
                    for (StatusMoYing statusMoYing : adder.getList()) {
                        statusMoYing.addMaxHp();
                    }
                }
            });
        }
    }

    @Override
    public void enable() {
        super.enable();
        Character belongTo = getBelongTo();
        int level = getLevel();
        double ratio = level >= 2 ? 1.3 : 1.2;
        boolean increaseEffectResist = level >= 3;
        adder = belongTo.bp().addStatusAdder(c ->
                c.team == belongTo.team
                        ? new StatusMoYing(belongTo, c, ratio, increaseEffectResist)
                        : null
        );
    }

    @Override
    public void disable() {
        super.disable();
        adder.deleteAndRemove();
        adder = null;
    }

    private static class StatusMoYing extends Status {
        protected StatusMoYing(Character from, Character belongTo, double ratio, boolean increaseEffectResist) {
            super("墨萦", from, belongTo, StatusType.BUFF, StatusForm.YIN_JI);
            runOn(Trigger.WHEN_HEAL, param ->
                    ((ParamHealInfo) param).healInfo.getTraceableNumber().mul(ratio, "墨萦"));
            if (increaseEffectResist) {
                attribute(Attribute.EFFECT_RESIST_RATE, 20);
            }
        }

        private void addMaxHp() {
            belongTo.setMaxHp(belongTo.getMaxHp() * 1.1, false);
        }
    }
}
