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
    public void init(PropertiesHolder propertiesHolder, BattlePane bp) {
        super.init(propertiesHolder, bp);

        // 免疫来源于其他目标的行动条改变效果
        addStatus(new StatusIgnoreOtherActionChange(this));
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
        skills.add(new Skill1(this, skill1Level));
        skills.add(new Skill3(this, skill3Level));
        skills.add(new Skill5(this));
        skills.add(new Skill6(this));
    }

    static class StatusIgnoreOtherActionChange extends Status implements IgnoreActionIncrease, IgnoreActionDecrease {
        public StatusIgnoreOtherActionChange(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean effective(Character from) {
            return from != belongTo;
        }
    }
}

