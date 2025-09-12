package com.mllfjn.simyys.character.sp.dayuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.starter.propertygetter.PropertiesMap;
import com.mllfjn.simyys.starter.propertygetter.PropertyInput;
import com.mllfjn.simyys.character.skill.Skill;
import javafx.collections.ObservableList;

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
        map.put("daYuan-skill1Level", new PropertyInput("技能1等级").setValue("5"));
        map.put("daYuan-skill3Level", new PropertyInput("技能3等级").setValue("5"));

        return map;
    }

    @Override
    public void init(PropertiesMap properties) {
        super.init(properties);

        skill1Level = properties.get("daYuan-skill1Level").getInt();
        skill3Level = properties.get("daYuan-skill3Level").getInt();
    }

    @Override
    public void addSkill(ObservableList<Skill> skills) {
        skills.add(new Skill1(this, skill1Level));
        skills.add(new Skill2TODO(this));
        skills.add(new Skill3TODO(this, skill3Level));
        skills.add(new Skill5(this));
        skills.add(new Skill6(this));
    }
}
