package com.mllfjn.simyys.character.list.yys.yuanlaiguang;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.character.list.yys.CharacterYYSBase;
import com.mllfjn.simyys.character.propertygetter.*;

public class YuanLaiGuang extends CharacterYYSBase {
    public static final String CharacterName = "源赖光";

    @Override
    public PropertiesMap getProperties() {
        PropertiesMap map = super.getProperties();

        map.get(PropertyKey.GENERAL_SPEED_KEY).setValue("142");
        map.get(PropertyKey.GENERAL_YU_HUN_ATTACK_KEY).setValue("5568");
        map.get(PropertyKey.GENERAL_HP_KEY).setValue("20826");
        map.get(PropertyKey.GENERAL_DEFENSE_KEY).setValue("806");
        map.get(PropertyKey.GENERAL_CRIT_RATE_KEY).setValue("20");

        return map;
    }

    @Override
    protected YYSSkill[] getYYSSkills() {
        return new YYSSkill[]{
                new YYSSkill(Skill1.SkillName, 5, 0),
                null,
                null,
                new YYSSkill(Skill4.SkillName, 5, 0),
                new YYSSkill(Skill5.SkillName, 5, 3),
                new YYSSkill(Skill6.SkillName, 1, 0),
                new YYSSkill(Skill7.SkillName, 5, 0),
                null,
        };
    }

    @Override
    public void init(PropertiesHolder propertiesHolder, BattlePane bp) {
        super.init(propertiesHolder, bp);

        PropertiesMap map = propertiesHolder.propertiesMap;

        int[] skillLevels = ((PropertyMultiInput) map.get(Skill1.SkillName)).getValuesInt();
        addSkill(new Skill1(this, skillLevels[0], skillLevels[1]));

        skillLevels = ((PropertyMultiInput) map.get(Skill4.SkillName)).getValuesInt();
        if (skillLevels[0] > 0) {
            addSkill(new Skill4(this, skillLevels[0], skillLevels[1]));
        }

        skillLevels = ((PropertyMultiInput) map.get(Skill5.SkillName)).getValuesInt();
        if (skillLevels[0] > 0) {
            addSkill(new Skill5(this, skillLevels[0], skillLevels[1]));
        }

        skillLevels = ((PropertyMultiInput) map.get(Skill6.SkillName)).getValuesInt();
        if (skillLevels[0] > 0) {
            addSkill(new Skill6(this, skillLevels[1]));
        }

        skillLevels = ((PropertyMultiInput) map.get(Skill7.SkillName)).getValuesInt();
        if (skillLevels[0] > 0) {
            addSkill(new Skill7(this, skillLevels[0], skillLevels[1]));
        }
    }
}
