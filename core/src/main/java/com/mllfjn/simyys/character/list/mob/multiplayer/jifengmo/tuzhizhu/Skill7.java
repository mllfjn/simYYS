package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.tuzhizhu;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;

class Skill7 extends PassiveSkill {
    private static final String SkillName = "坚韧";

    private final Status status;

    private int count = 0;

    Skill7(Character belongTo) {
        super(belongTo, -1, 7);
        status = Status.of(SkillName, belongTo);
        status.runOn(Trigger.BEING_ATTACKED, triggerParam ->
                ((ParamAttackInfo) triggerParam).getAttackInfo().getTraceableNumber().mul(0.3, SkillName)
        );
    }

    void tZZReduceEnable() {
        if (count == 0) {
            getBelongTo().addStatus(status);
        }
        count++;
    }

    void tZZReduceDisable() {
        count--;
        if (count == 0) {
            getBelongTo().removeStatus(status);
        }
    }

    @Override
    public String getSkillDesc() {
        return "\t场上有白茧或小蜘蛛召唤物时,土蜘蛛受到的伤害降低70%";
    }

    @Override
    public String getName() {
        return SkillName;
    }
}
