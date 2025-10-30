package com.mllfjn.simyys.character.yys.shenle;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.propertygetter.PropertiesMap;
import com.mllfjn.simyys.character.propertygetter.PropertyInput;
import com.mllfjn.simyys.character.yys.PropertyYYS;

public class ShenLe extends Character {
    public static final String privateName = "神乐";
    private static final String SKILL1_KEY = "普攻·伞击等级";
    private static final String SKILL_JF_KEY = "通灵·疾风等级";
    private int skill1Level;
    private int skillJiFengLevel;
    public ShenLe() {

    }

    @Override
    public PropertiesMap getProperties() {
        PropertiesMap map = super.getProperties();

        map.put(SKILL1_KEY, new PropertyInput().setValue("5"));
//        map.put(SKILL_JF_KEY, new PropertyInput().setValue("5"));
        PropertyYYS.changeDefaultProperty(map);

        return map;
    }

    @Override
    public void init(PropertiesMap properties, BattlePane bp) {
        super.init(properties, bp);
        skill1Level = properties.get(SKILL1_KEY).getInt();
//        skillJiFengLevel = properties.get(SKILL_JF_KEY).getInt();
    }

    @Override
    public void addSkills() {
        super.addSkills();
        skills.add(new SkillPuGong(this, skill1Level));
    }
}
