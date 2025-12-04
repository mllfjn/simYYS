package com.mllfjn.simyys.character.sp.dayuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.propertygetter.PropertiesHolder;
import com.mllfjn.simyys.character.CharacterShiShenBase;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;
import com.mllfjn.simyys.character.status.determinant.IgnoreActionDecrease;
import com.mllfjn.simyys.character.status.determinant.IgnoreActionIncrease;

public class DaYuan extends CharacterShiShenBase {
    public static final String CharacterName = "纺愿缘结神";
    public DaYuan() {

    }

    @Override
    protected boolean useSkillAuto() {
        return isHaveStatus(StatusCombined.class) ? tryUseSkill(3) : tryUseSkill(5);
    }

    @Override
    protected String getDefaultBaseAttack() {
        return "2224";
    }

    @Override
    protected String getDefaultSkillLevel() {
        return "515";
    }

    @Override
    protected boolean canAwakening() {
        return false;
    }

    @Override
    public void addOwnSkills() {
        addSkill(new Skill1(this, skill1Level));
        Skill2.addStatus(this);
        addSkill(new Skill3(this, skill3Level));
        addSkill(new Skill5(this));
        addSkill(new Skill6(this));
    }
}

