package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.tuzhizhu;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.PassiveSkillCanNotSeal;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;
import com.mllfjn.simyys.character.status.determinant.InfluenceDamageBeingAttack;
import com.mllfjn.simyys.interactive.AttackInfo;

class Skill7 extends PassiveSkillCanNotSeal {
    private static final String SkillName = "坚韧";

    private final StatusTZZJRReduceDamage status;

    private int count = 0;

    Skill7(Character belongTo) {
        super(belongTo, -1, 7);
        status = new StatusTZZJRReduceDamage(belongTo);
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

    static class StatusTZZJRReduceDamage extends Status implements InfluenceDamageBeingAttack {
        public StatusTZZJRReduceDamage(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public void doInfluenceBeingAttack(AttackInfo attackInfo) {
            attackInfo.getTraceableNumber().mul(0.3, SkillName);
        }
    }
}
