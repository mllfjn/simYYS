package com.mllfjn.simyys.character.list.ssr.guiqie;

import com.mllfjn.simyys.character.CharacterShiShenBase;
import com.mllfjn.simyys.utils.Utils;

public class GuiQie extends CharacterShiShenBase {
    public static final String CharacterName = "鬼切";

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
        return "3350";
    }

    @Override
    protected void addOwnSkills() {
        if (isMob()) {
            Skill2ForMob skill2 = new Skill2ForMob(this);
            addSkill(new Skill1ForMob(this, skill1Level, skill2));
            addSkill(skill2);
            addSkill(new Skill3ForMob(this, skill3Level, skill2));
        } else {
            Utils.information("新版鬼切技能还没做,设置为怪物以使用旧版技能");
        }
    }
}
