package com.mllfjn.simyys.character.ssr.namei;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.starter.propertygetter.PropertiesMap;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.starter.propertygetter.PropertyInput;
import javafx.collections.ObservableList;

public class NaMei extends Character {
    public static final String privateName = "伊邪那美";
    private int skill1Level;
    private int skill2Level;
    public NaMei() {

    }

    @Override
    public PropertiesMap getProperties() {
        PropertiesMap map = super.getProperties();
        map.put("naMei-Skill1Level", new PropertyInput("技能1等级").setValue("5"));
        map.put("naMei-Skill2Level", new PropertyInput("技能2等级").setValue("5"));
        return map;
    }

    @Override
    public void init(PropertiesMap properties) {
        super.init(properties);
        skill1Level = properties.get("naMei-Skill1Level").getInt();
        skill2Level = properties.get("naMei-Skill2Level").getInt();
    }

    @Override
    public void addSkill(ObservableList<Skill> skills) {
        skills.add(new Skill1(this, skill1Level));
        skills.add(new Skill2TODO(this));
    }

    @Override
    public void useFrontSkill(BattlePane bp) {
        if (skill2Level == 5) {
            Skill2TODO skill2 = (Skill2TODO) getSkill(2);
            skill2.useFront(bp);
        }
    }
}
