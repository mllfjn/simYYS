package com.mllfjn.simyys.character.list.yys.shenle;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.propertygetter.PropertiesHolder;
import com.mllfjn.simyys.character.propertygetter.PropertiesMap;
import com.mllfjn.simyys.character.list.yys.CharacterYYSBase;
import com.mllfjn.simyys.character.propertygetter.PropertyMultiInput;

public class ShenLe extends CharacterYYSBase {
    public static final String CharacterName = "神乐";

    @Override
    protected YYSSkill[] getYYSSkills() {
        return new YYSSkill[]{
                new YYSSkill(Skill1.SkillName, 5, 0),
                null,
                new YYSSkill(Skill3.SkillName, 5, 3),
                null,
                null,
                null,
                null,
                null,
        };
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
}
