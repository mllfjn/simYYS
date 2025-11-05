package com.mllfjn.simyys.character.yys.shenle;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.character.propertygetter.PropertiesHolder;
import com.mllfjn.simyys.character.propertygetter.PropertiesMap;
import com.mllfjn.simyys.character.propertygetter.PropertyInput;
import com.mllfjn.simyys.character.yys.CharacterYYSBase;

public class ShenLe extends CharacterYYSBase {
    public static final String CharacterName = "神乐";
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

        return map;
    }

    @Override
    public void init(PropertiesHolder propertiesHolder, BattlePane bp) {
        super.init(propertiesHolder, bp);
        skill1Level = propertiesHolder.map.get(SKILL1_KEY).getInt();
//        skillJiFengLevel = properties.get(SKILL_JF_KEY).getInt();
    }

    @Override
    public void addOwnSkills() {
        skills.add(new Skill1(this, skill1Level));
    }
}
