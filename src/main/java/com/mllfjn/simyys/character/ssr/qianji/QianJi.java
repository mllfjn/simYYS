package com.mllfjn.simyys.character.ssr.qianji;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.character.propertygetter.PropertiesMap;
import com.mllfjn.simyys.character.propertygetter.PropertyInput;

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
        ((PropertyInput) map.get(PropertyKey.GENERAL_BASE_ATTACK_KEY)).setValue("2948");
        map.put(PropertyKey.SKILL_KEY, new PropertyInput().setValue("555"));

        return map;
    }

    @Override
    public void init(PropertiesMap properties, BattlePane bp) {
        super.init(properties, bp);
        int skill = properties.get(PropertyKey.SKILL_KEY).getInt();
        skill1Level = PropertyKey.getSkillLevel(skill, 1);
        skill3Level = PropertyKey.getSkillLevel(skill, 3);
    }

    @Override
    public void addSkills() {
        super.addSkills();
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
