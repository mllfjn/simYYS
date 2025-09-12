package com.mllfjn.simyys.character.ssr.xiaoyuan;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.starter.propertygetter.PropertiesMap;
import com.mllfjn.simyys.starter.propertygetter.PropertyInput;
import com.mllfjn.simyys.character.skill.Skill;
import javafx.collections.ObservableList;

public class XiaoYuan extends Character {
    public static final String privateName = "缘结神";
    private int skill1Level;
    public XiaoYuan() {

    }

    @Override
    public PropertiesMap getProperties() {
        PropertiesMap map = super.getProperties();
        map.put("xiaoYuan-skill1Level", new PropertyInput("技能1等级").setValue("5"));
        return map;
    }

    @Override
    public void init(PropertiesMap properties) {
        super.init(properties);
        skill1Level = properties.get("xiaoYuan-skill1Level").getInt();
    }

    @Override
    public void addSkill(ObservableList<Skill> skills) {
        skills.add(new Skill1(this, skill1Level));
    }

}
