package com.mllfjn.simyys.character.sp.dayuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.character.propertygetter.PropertiesMap;
import com.mllfjn.simyys.character.propertygetter.PropertyInput;
import com.mllfjn.simyys.state.State;
import com.mllfjn.simyys.state.StateForm;
import com.mllfjn.simyys.state.StateType;
import com.mllfjn.simyys.state.determinant.IgnoreActionDecrease;
import com.mllfjn.simyys.state.determinant.IgnoreActionIncrease;

public class DaYuan extends Character {
    public static final String privateName = "纺愿缘结神";
    private int skill1Level;
    private int skill3Level;
    public DaYuan() {

    }

    @Override
    protected boolean useSkillAuto(BattlePane bp) {
        return getSkill(5).tryUse(bp) ||
                getSkill(3).tryUse(bp);
    }

    @Override
    public PropertiesMap getProperties() {
        PropertiesMap map = super.getProperties();

        ((PropertyInput) map.get(PropertyKey.GENERAL_BASE_ATTACK_KEY)).setValue("2224");

        map.put(PropertyKey.SKILL_KEY, new PropertyInput().setValue("515"));

        return map;
    }

    @Override
    public void init(PropertiesMap properties, BattlePane bp) {
        super.init(properties, bp);
        int skillLevel = properties.get(PropertyKey.SKILL_KEY).getInt();
        skill1Level = PropertyKey.getSkillLevel(skillLevel, 1);
        skill3Level = PropertyKey.getSkillLevel(skillLevel, 3);

        // 免疫来源于其他目标的行动条改变效果
        addState(new StateIgnoreOtherActionChange(this));
    }

    @Override
    public void addSkills() {
        super.addSkills();
        skills.add(new Skill1(this, skill1Level));
        skills.add(new Skill3TODO(this, skill3Level));
        skills.add(new Skill5(this));
        skills.add(new Skill6(this));
    }
}

class StateIgnoreOtherActionChange extends State implements IgnoreActionIncrease, IgnoreActionDecrease {
    public StateIgnoreOtherActionChange(Character character) {
        super(character, character, StateType.SPECIAL, StateForm.SPECIAL);
    }

    @Override
    public boolean effective(Character from) {
        return from != belongTo;
    }

    @Override
    public void setName() {
        name = "大缘免疫推拉条";
    }
}