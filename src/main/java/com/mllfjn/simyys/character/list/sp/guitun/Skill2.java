package com.mllfjn.simyys.character.list.sp.guitun;

import com.mllfjn.simyys.battleevent.StatusAdder;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;
import com.mllfjn.simyys.character.status.Trigger;
import com.mllfjn.simyys.character.status.triggerParam.ParamAddCrowdControl;
import com.mllfjn.simyys.character.status.triggerParam.ParamHealInfo;

class Skill2 extends PassiveSkill {
    private static final String SkillName = "红莲";

    private static final double[] HEAL = new double[]{0, 0.5, 0.6, 0.6, 0.7, 0.7};

    private final Status status;
    private StatusAdder<?> adder;

    public Skill2(Character belongTo, int level) {
        super(belongTo, level, 2);
        status = Status.of(SkillName, belongTo)
                // TODO 放逐
                .runOn(Trigger.ADDING_CROWD_CONTROL, param ->
                        ((ParamAddCrowdControl) param).getEffectInfo().setCancel(true))
                .runOn(Trigger.WHEN_HEAL, param ->
                        ((ParamHealInfo) param).healInfo.getTraceableNumber().mul(HEAL[level], SkillName))
                .retainAfterDie()
                .retainAfterChangeWave();
    }

    @Override
    public void enable() {
        Character belongTo = getBelongTo();
        belongTo.addStatus(status);

        boolean jianShang = getLevel() >= 3;
        boolean selfDouble = getLevel() >= 5;

        adder = belongTo.bp().addStatusAdder(c -> c.team == belongTo.team
                ? new StatusDaYaoZhiLi(belongTo, c, jianShang, selfDouble)
                : null
        );
    }

    @Override
    public void disable() {
        getBelongTo().replaceStatus(status);
        adder.deleteAndRemove();
    }

    @Override
    public String getName() {
        return SkillName;
    }

    private static class StatusDaYaoZhiLi extends Status {
        protected StatusDaYaoZhiLi(Character from, Character belongTo, boolean jianShang, boolean selfDouble) {
            super("大妖之力", from, belongTo, StatusType.BUFF, StatusForm.YIN_JI);
            display("妖");
            attribute(Attribute.ZENG_SHANG, _ ->
                    (double) belongTo.getLostHpPercentage() * (selfDouble && from == belongTo ? 2 : 1));
            if (jianShang) {
                attribute(Attribute.JIAN_SHANG, _ ->
                        (double) belongTo.getLostHpPercentage() * (selfDouble && from == belongTo ? 2 : 1));
            }
        }
    }
}
