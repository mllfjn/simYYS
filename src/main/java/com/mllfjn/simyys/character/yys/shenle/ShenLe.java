package com.mllfjn.simyys.character.yys.shenle;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.starter.propertygetter.PropertiesMap;
import com.mllfjn.simyys.starter.propertygetter.PropertyCheck;
import com.mllfjn.simyys.starter.propertygetter.PropertyInput;
import com.mllfjn.simyys.character.skill.Skill;
import javafx.collections.ObservableList;

public class ShenLe extends Character {
    public static final String privateName = "神乐";
    private int skill1Level;
    public ShenLe() {

    }

    @Override
    public PropertiesMap getProperties() {
        PropertiesMap map = super.getProperties();

        ((PropertyCheck) map.get(Character.GENERAL_YYS_KEY)).setValue(true);
        ((PropertyInput) map.get(Character.GENERAL_SPEED_KEY)).setValue("128");

        map.put("shenLe_Skill1Level", new PropertyInput("技能1等级").setValue("5"));
        map.put("shenLe_skillJFLevel", new PropertyInput("疾风等级").setValue("5"));

        return map;
    }

    @Override
    public void init(PropertiesMap properties) {
        super.init(properties);
        skill1Level = properties.get("shenLe_Skill1Level").getInt();
        properties.get("shenLe_skillJFLevel").getInt();
    }

    @Override
    public void addSkill(ObservableList<Skill> skills) {
        skills.add(new SkillPuGong(this, skill1Level));
    }
}
