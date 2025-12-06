package com.mllfjn.simyys.character.list.yys.shenle;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.character.propertygetter.PropertiesHolder;
import com.mllfjn.simyys.character.propertygetter.PropertiesMap;
import com.mllfjn.simyys.character.list.yys.CharacterYYSBase;
import com.mllfjn.simyys.character.propertygetter.PropertyMultiInput;

public class ShenLe extends CharacterYYSBase {
    public static final String CharacterName = "神乐";
    public static final String[] SkillNames = new String[]{Skill1.SkillName, Skill3.SkillName};
    public static final String[] DefaultSkillLevels = new String[]{"5", "5"};
    public static final String[] DefaultShuYin = new String[]{"0", "3"};

    public ShenLe() {

    }

    @Override
    public PropertiesMap getProperties() {
        PropertiesMap map = super.getProperties();

        PropertyKey.addYYSSkill(map, SkillNames, DefaultSkillLevels, DefaultShuYin);

        return map;
    }

    @Override
    public void init(PropertiesHolder propertiesHolder, BattlePane bp) {
        super.init(propertiesHolder, bp);

        PropertiesMap map = propertiesHolder.propertiesMap;

        int[] skill = ((PropertyMultiInput) map.get(Skill1.SkillName)).getValuesInt();
        addSkill(new Skill1(this, skill[0], skill[1]));

        skill = ((PropertyMultiInput) map.get(Skill3.SkillName)).getValuesInt();
        if (skill[0] > 0) {
            addSkill(new Skill3(this, skill[0], skill[1]));
        }
    }

    @Override
    public void addOwnSkills() {

    }
}
