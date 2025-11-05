package com.mllfjn.simyys.character.sp.dayuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.propertygetter.PropertiesHolder;
import com.mllfjn.simyys.character.CharacterShiShenBase;
import com.mllfjn.simyys.state.State;
import com.mllfjn.simyys.state.StateForm;
import com.mllfjn.simyys.state.StateType;
import com.mllfjn.simyys.state.determinant.IgnoreActionDecrease;
import com.mllfjn.simyys.state.determinant.IgnoreActionIncrease;

public class DaYuan extends CharacterShiShenBase {
    public static final String CharacterName = "纺愿缘结神";
    private int skill1Level;
    private int skill3Level;
    public DaYuan() {

    }

    @Override
    protected boolean useSkillAuto() {
        return isHaveState(StateCombined.class) ? tryUseSkill(3) : tryUseSkill(5);
    }

    @Override
    protected String getDefaultBaseAttack() {
        return "2224";
    }

    @Override
    public void init(PropertiesHolder propertiesHolder, BattlePane bp) {
        super.init(propertiesHolder, bp);

        // 免疫来源于其他目标的行动条改变效果
        addState(new StateIgnoreOtherActionChange(this));
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

    static class StateIgnoreOtherActionChange extends State implements IgnoreActionIncrease, IgnoreActionDecrease {
        public StateIgnoreOtherActionChange(Character character) {
            super(character, character, StateType.SPECIAL, StateForm.SPECIAL);
        }

        @Override
        public boolean effective(Character from) {
            return from != belongTo;
        }
    }
}

