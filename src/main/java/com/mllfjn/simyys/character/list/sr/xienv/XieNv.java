package com.mllfjn.simyys.character.list.sr.xienv;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.CharacterShiShenBase;

public class XieNv extends CharacterShiShenBase {
    public static final String CharacterName = "蝎女";

    private Character xieDuCarrier;

    public Character getXieDuCarrier() {
        return xieDuCarrier;
    }

    public void setXieDuCarrier(Character xieDuCarrier) {
        this.xieDuCarrier = xieDuCarrier;
    }

    @Override
    protected String getDefaultSkillLevel() {
        return "555";
    }

    @Override
    protected boolean canAwakening() {
        return true;
    }

    @Override
    protected String getDefaultBaseAttack() {
        return "2975";
    }

    @Override
    protected void addOwnSkills() {
        addSkill(new Skill1(this, skill1Level));
        addSkill(new Skill2(this, skill2Level));
        addSkill(new Skill3(this, skill3Level));
    }
}
