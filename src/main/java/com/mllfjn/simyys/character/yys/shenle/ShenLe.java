package com.mllfjn.simyys.character.yys.shenle;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.character.propertygetter.PropertiesHolder;
import com.mllfjn.simyys.character.propertygetter.PropertiesMap;
import com.mllfjn.simyys.character.propertygetter.PropertyMultiInput;
import com.mllfjn.simyys.character.yys.CharacterYYSBase;
import com.mllfjn.simyys.utils.Utils;

public class ShenLe extends CharacterYYSBase {
    public static final String CharacterName = "神乐";
    private static final String SKILL1_KEY = "普攻·伞击等级";
    private static final String SKILL_JF_KEY = "通灵·疾风等级";

    private Skill1 skill1;
    private SkillJF skillJF;

    public ShenLe() {

    }

    @Override
    public PropertiesMap getProperties() {
        PropertiesMap map = super.getProperties();
        map.put(SKILL1_KEY, PropertyKey.getYYSSkillPMI());
        map.put(SKILL_JF_KEY, PropertyKey.getYYSSkillPMI());

        return map;
    }

    @Override
    public void init(PropertiesHolder propertiesHolder, BattlePane bp) {
        super.init(propertiesHolder, bp);
        String[] skill1Values = ((PropertyMultiInput) propertiesHolder.propertiesMap.get(SKILL1_KEY)).getValues();
        skill1 = new Skill1(this
                , Utils.parseIntOrDefault(skill1Values[0], 0)
                , Utils.parseIntOrDefault(skill1Values[1], 0)
        );

        String[] skillJFValues = ((PropertyMultiInput) propertiesHolder.propertiesMap.get(SKILL_JF_KEY)).getValues();
        skillJF = new SkillJF(this
                , Utils.parseIntOrDefault(skillJFValues[0], 0)
                , Utils.parseIntOrDefault(skillJFValues[1], 0)
        );
    }

    @Override
    public void addOwnSkills() {
        skills.add(skill1);
        skills.add(skillJF);
    }
}
