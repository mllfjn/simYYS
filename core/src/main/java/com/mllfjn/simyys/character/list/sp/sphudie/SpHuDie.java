package com.mllfjn.simyys.character.list.sp.sphudie;

import com.mllfjn.simyys.character.CharacterShiShenBase;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.instance.StatusSleep;

public class SpHuDie extends CharacterShiShenBase {
    public static final String CharacterName = "梦引蝴蝶精";

    private Skill2 skill2;

    @Override
    protected boolean useSkillAuto() {
        return tryUseSkill(3);
    }

    @Override
    public <T extends Status> boolean addStatus(T newStatus) {
        if (newStatus instanceof StatusSleep) {
            skill2.huDieSkill2Use();
            return false;
        } else {
            return super.addStatus(newStatus);
        }
    }

    @Override
    protected String getDefaultSkillLevel() {
        return "555";
    }

    @Override
    protected boolean canAwakening() {
        return false;
    }

    @Override
    protected String getDefaultBaseAttack() {
        return "";
    }

    @Override
    protected void addOwnSkills() {
        skill2 = new Skill2(this, skill2Level);
        addSkill(new Skill1(this, skill1Level));
        addSkill(skill2);
        addSkill(new Skill3(this, skill3Level, skill2));
    }
}
