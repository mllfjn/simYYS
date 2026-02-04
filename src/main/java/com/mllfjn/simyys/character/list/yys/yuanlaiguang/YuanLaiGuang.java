package com.mllfjn.simyys.character.list.yys.yuanlaiguang;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.character.list.yys.qiling.QiLingFactory;
import com.mllfjn.simyys.character.propertygetter.*;

public class YuanLaiGuang extends Character {
    public static final String CharacterName = "源赖光";

    private static final String[] SkillNames = new String[]{
            Skill1.SkillName, Skill4.SkillName, Skill5.SkillName, Skill6Passive.SkillName, Skill7Passive.SkillName};
    private static final String[] DefaultSkillLevels = new String[]{"5", "5", "5", "1", "5"};
    private static final String[] DefaultShuYin = new String[]{"0", "0", "3", "0", "0"};

    @Override
    public PropertiesMap getProperties() {
        PropertiesMap map = super.getProperties();

        ((PropertyInput) map.get(PropertyKey.GENERAL_SPEED_KEY)).setValue("142");
        ((PropertyInput) map.get(PropertyKey.GENERAL_YU_HUN_ATTACK_KEY)).setValue("5568");
        ((PropertyInput) map.get(PropertyKey.GENERAL_HP_KEY)).setValue("20826");
        ((PropertyInput) map.get(PropertyKey.GENERAL_DEFENSE_KEY)).setValue("806");
        ((PropertyInput) map.get(PropertyKey.GENERAL_CRIT_RATE_KEY)).setValue("20");
        ((PropertyInput) map.get(PropertyKey.GENERAL_CRIT_POWER_KEY)).setValue("150");
        ((PropertyInput) map.get(PropertyKey.GENERAL_EFFECT_HIT_RATE_KEY)).setValue("0");
        ((PropertyInput) map.get(PropertyKey.GENERAL_EFFECT_RESIST_RATE_KEY)).setValue("0");

        map.put(PropertyKey.QI_LING_KEY, new PropertySelectSingle(QiLingFactory.QI_LING));

        ((PropertyCheck) map.get(PropertyKey.GENERAL_YYS_KEY)).setValue(true);

        PropertyKey.addYYSSkill(map, SkillNames, DefaultSkillLevels, DefaultShuYin);

        return map;
    }

    @Override
    public void init(PropertiesHolder propertiesHolder, BattlePane bp) {
        super.init(propertiesHolder, bp);

        PropertiesMap map = propertiesHolder.propertiesMap;

        int[] skill = ((PropertyMultiInput) map.get(Skill1.SkillName)).getValuesInt();
        addSkill(new Skill1(this, skill[0], skill[1]));

        skill = ((PropertyMultiInput) map.get(Skill4.SkillName)).getValuesInt();
        if (skill[0] > 0) {
            addSkill(new Skill4(this, skill[0], skill[1]));
        }

        skill = ((PropertyMultiInput) map.get(Skill5.SkillName)).getValuesInt();
        if (skill[0] > 0) {
            addSkill(new Skill5(this, skill[0], skill[1]));
        }

        skill = ((PropertyMultiInput) map.get(Skill6Passive.SkillName)).getValuesInt();
        if (skill[0] > 0) {
            addSkill(new Skill6Passive(this, skill[1]));
        }

        skill = ((PropertyMultiInput) map.get(Skill7Passive.SkillName)).getValuesInt();
        if (skill[0] > 0) {
            addSkill(new Skill7Passive(this, skill[0], skill[1]));
        }
    }

    @Override
    protected String getDefaultBaseAttack() {
        return "3256";
    }

    @Override
    protected void addOwnSkills() {
    }
}
