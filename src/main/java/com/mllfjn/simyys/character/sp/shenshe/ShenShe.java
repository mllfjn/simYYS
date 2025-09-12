package com.mllfjn.simyys.character.sp.shenshe;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.starter.propertygetter.PropertiesMap;
import com.mllfjn.simyys.starter.propertygetter.PropertyInput;
import com.mllfjn.simyys.character.skill.Skill;
import javafx.collections.ObservableList;

public class ShenShe extends Character {
    public static final String privateName = "神堕八岐大蛇";

    private int skill1Level;
    private int skill2Level;
    private int skill3Level;
    public ShenShe() {}

    @Override
    public PropertiesMap getProperties() {
        PropertiesMap map = super.getProperties();
        map.put("shenShe-skill1Level", new PropertyInput("技能1等级").setValue("5"));
        map.put("shenShe-skill2Level", new PropertyInput("技能2等级").setValue("5"));
        map.put("shenShe-skill3Level", new PropertyInput("技能3等级").setValue("5"));

        return map;
    }

    @Override
    public void init(PropertiesMap properties) {
        super.init(properties);
        skill1Level = properties.get("shenShe-skill1Level").getInt();
        skill2Level = properties.get("shenShe-skill2Level").getInt();
        skill3Level = properties.get("shenShe-skill3Level").getInt();
    }

    @Override
    public void addSkill(ObservableList<Skill> skills) {
        skills.add(new SkillPuGong(this, skill1Level));
    }

}
