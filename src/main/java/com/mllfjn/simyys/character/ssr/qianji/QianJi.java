package com.mllfjn.simyys.character.ssr.qianji;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.starter.propertygetter.PropertiesMap;
import com.mllfjn.simyys.starter.propertygetter.PropertyInput;
import com.mllfjn.simyys.character.skill.Skill;
import javafx.collections.ObservableList;

public class QianJi extends Character {
    public static final String privateName = "千姬";
    private boolean havePutDown = false;
    private int skill1Level;
    private int skill3Level;
    public QianJi() {

    }

    @Override
    public PropertiesMap getProperties() {
        PropertiesMap map = super.getProperties();
        map.put("qianJi-skill1Level", new PropertyInput("技能1等级").setValue("5"));
        map.put("qianJi-skill3Level", new PropertyInput("技能3等级").setValue("5"));

        return map;
    }

    @Override
    public void init(PropertiesMap properties) {
        super.init(properties);
        skill1Level = properties.get("qianJi-skill1Level").getInt();
        skill3Level = properties.get("qianJi-skill3Level").getInt();
    }

    @Override
    public void addSkill(ObservableList<Skill> skills) {
        skills.add(new Skill1TODO(this, skill1Level));
        skills.add(new Skill3PutTODO(this, skill3Level));
    }

    public boolean isHavePutDown() {
        return havePutDown;
    }

    public void setHavePutDown(boolean havePutDown) {
        this.havePutDown = havePutDown;
    }
}
